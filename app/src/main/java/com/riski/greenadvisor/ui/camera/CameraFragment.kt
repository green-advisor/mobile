package com.riski.greenadvisor.ui.camera

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleOwner
import com.riski.greenadvisor.BetaActivity
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.response.DataLogin
import com.riski.greenadvisor.databinding.FragmentCameraBinding
import com.riski.greenadvisor.ui.detail.detailcamera.DetailCameraDetectActivity
import com.riski.greenadvisor.ui.home.HomeViewModel
import com.riski.greenadvisor.ui.home.HomeViewModelFactory
import com.riski.greenadvisor.ui.home.dataStore
import com.riski.greenadvisor.utils.createFile
import com.riski.greenadvisor.utils.reduceFileImage
import com.riski.greenadvisor.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class CameraFragment : Fragment(), LifecycleOwner {

    private lateinit var dataStore: DataStore<Preferences>
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding
    private lateinit var previewView: PreviewView
    private lateinit var camera: Camera
    private var captureImg: ImageCapture? = null
    private var isPreviewShown = false
    private var isFlashOn = false
    private var getFile: File? = null
    private lateinit var user: DataLogin
    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var homeViewModel: HomeViewModel


    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if  (requestCode == REQUEST_CODE_PERMISSIONS) {
            if(!allPermissionGrant()) {
                Toast.makeText(requireContext(), getString(R.string.invalid), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()

        previewView = binding?.cameraPreview!!
        getPermission()
        homeViewModel1()
        cameraViewModel1()
        flashFun()
        changeCamera()
        cameraInfo()

        binding?.cameraPress?.setOnClickListener { takeImg() }
        binding?.cameraGalery?.setOnClickListener { startGallery() }
        binding?.cameraBtn?.setOnClickListener { uploadImage() }

        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataStore = requireContext().dataStore
    }

    private fun homeViewModel1() {
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(Preference.getInstance(dataStore), requireContext()))[HomeViewModel::class.java]

        homeViewModel.getUser().observe(requireActivity()) {
            user = DataLogin(
                it.name,
                it.token,
                true
            )
        }
    }

    private fun cameraViewModel1() {
        cameraViewModel = ViewModelProvider(this, CameraViewModelFactory(Preference.getInstance(dataStore), requireContext()))[CameraViewModel::class.java]

    }

    private fun allPermissionGrant() = REQUIRES_PERMISSION.all{
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getPermission() {
        if (!allPermissionGrant()) {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRES_PERMISSION, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post { startCamera() }
        binding?.cameraFocus?.setOnClickListener {
            focusCamera()
        }

        if (isPreviewShown) {
            binding?.cameraPress?.visibility = View.GONE
            binding?.cameraBtn?.visibility = View.GONE
            binding?.cameraFocus?.visibility = View.GONE
            binding?.cameraFlash?.visibility = View.GONE
            binding?.cameraGalery?.visibility = View.GONE
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_image))
        intentGalery.launch(chooser)
    }

    private val intentGalery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val imgSelect: Uri = it.data?.data as Uri
            val myFile = uriToFile(imgSelect, requireContext())
            getFile = myFile
            binding?.previewImage?.setImageURI(imgSelect)

            binding?.cameraFocus?.visibility = View.GONE
            binding?.cameraGalery?.visibility = View.GONE
            binding?.cameraFlash?.visibility = View.GONE
            binding?.cameraPress?.visibility = View.GONE
            binding?.cameraBtn?.visibility = View.VISIBLE

            isPreviewShown = true
        }
    }

    private fun cameraInfo() {
        Handler(Looper.getMainLooper()).postDelayed( {
            binding?.cameraUpdateInfo?.visibility = View.GONE
        }, 1500L)
    }

    private fun changeCamera() {
        binding?.cameraChange2?.setOnClickListener {
        startActivity(Intent(requireContext(), BetaActivity::class.java))
         }
    }

    private fun flashFun() {
        binding?.cameraFlash?.setOnClickListener {
            if (isFlashOn) {
               turnFlashOff()
            } else {
                turnFlashOn()
            }
        }
    }

    private fun turnFlashOn() {
        val cameraFlash = camera.cameraControl
        val cameraInfo = camera.cameraInfo
        if (cameraInfo.hasFlashUnit()) {
            cameraFlash.enableTorch(true)
            binding?.cameraFlash?.setImageResource(R.drawable.ic_baseline_flash_on_24)
            isFlashOn = true
        }
    }

    private fun turnFlashOff() {
        val cameraFlash = camera.cameraControl
        cameraFlash.enableTorch(false)
        binding?.cameraFlash?.setImageResource(R.drawable.ic_baseline_flash_off_24)
        isFlashOn = false
    }

    private fun startCamera()  {
        val cameraProviderfuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderfuture.addListener( {
            val cameraProvider = cameraProviderfuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            captureImg = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

           cameraProvider?.let {
               camera = it.bindToLifecycle(this, cameraSelector, preview, captureImg)
           }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takeImg() {
        val imageCapture = captureImg?: return
        val myFile = createFile(requireContext().applicationContext)
        getFile = myFile
        val outputOptions = ImageCapture.OutputFileOptions.Builder(myFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(myFile.absolutePath)
                    binding?.previewImage?.setImageBitmap(bitmap)
                    Log.e("File", "onFailure: $myFile")

                    binding?.cameraFocus?.visibility = View.GONE
                    binding?.cameraGalery?.visibility = View.GONE
                    binding?.cameraFlash?.visibility = View.GONE
                    binding?.cameraPress?.visibility = View.GONE
                    binding?.cameraBtn?.visibility = View.VISIBLE

                    isPreviewShown = true
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Image Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun focusCamera() {
        val cameraControl = camera.cameraControl
        val autoFocus = SurfaceOrientedMeteringPointFactory(1f, 1f).createPoint(0.5f, 0.5f)
        val action = FocusMeteringAction.Builder(autoFocus, FocusMeteringAction.FLAG_AF)
            .apply {
                disableAutoCancel()
            }
            .build()
        cameraControl.startFocusAndMetering(action)
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val imgMultipart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            cameraViewModel.getAllCamera(user.token, imgMultipart)
            Log.d("CameraFragment", "getFile: $getFile")
            cameraViewModel.response.observe(requireActivity()) { camera ->
                if (camera != null) {
                    if (camera.status) {
                        if (camera.data.isNotEmpty()) {
                        val dataCamera = camera.data[0]
                        val intent = Intent(requireContext(), DetailCameraDetectActivity::class.java)
                        intent.putExtra(DetailCameraDetectActivity.EXTRA_CAMERA, dataCamera)
                        startActivity(intent)
                        Toast.makeText(requireContext(), getString(R.string.camera_success), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.no_data_camera), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.camera_fail), Toast.LENGTH_SHORT).show()
                     }
                }
                proggressBar()
            }
        }
    }

    private fun proggressBar() {
        cameraViewModel.loading.observe(requireActivity()) {
            binding?.apply {
                if (it) {
                    cameraLoading.visibility = View.VISIBLE
                    cameraBtn.visibility = View.GONE
                } else {
                    cameraLoading.visibility = View.GONE
                    cameraBtn.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRES_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }

}