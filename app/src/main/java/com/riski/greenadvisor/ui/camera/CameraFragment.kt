package com.riski.greenadvisor.ui.camera

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
import com.riski.greenadvisor.R
import com.riski.greenadvisor.databinding.FragmentCameraBinding
import com.riski.greenadvisor.utils.createFile
import com.riski.greenadvisor.utils.showMesage
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment(), LifecycleOwner {

    private lateinit var viewModel: CameraViewModel
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private lateinit var camera: Camera
    private var captureImg: ImageCapture? = null
    private var isPreviewShown = false
    private var selectCamera: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        viewModel = ViewModelProvider(this)[CameraViewModel::class.java]
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()

        cameraExecutor = Executors.newSingleThreadExecutor()

        previewView = binding.cameraPreview
        getPermission()
        binding.cameraPress.setOnClickListener { takeImg() }
        return binding.root
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
        binding.cameraFocus.setOnClickListener {
            focusCamera()
        }

        if (isPreviewShown) {
            binding.cameraPress.visibility = View.GONE
            binding.cameraBtn.visibility = View.GONE
            binding.cameraFocus.visibility = View.GONE
            binding.cameraFlash.visibility = View.GONE
            binding.cameraGalery.visibility = View.GONE
        }
    }

    private fun startCamera()  {
        val cameraProviderfuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderfuture.addListener( {
            val cameraProvider = cameraProviderfuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            captureImg = ImageCapture.Builder().build()

            val imageanalizer = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(cameraExecutor, MyAnalyzer())
            }
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

           cameraProvider?.let {
               camera = it.bindToLifecycle(this, cameraSelector, preview, captureImg, imageanalizer)
           }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takeImg() {
        val imageCapture = captureImg?: return
        val photoFile = File(
            requireContext().externalMediaDirs.firstOrNull(), "${System.currentTimeMillis()}.jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    Toast.makeText(requireContext(),"Image Saved: $savedUri", Toast.LENGTH_SHORT).show()

                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    binding.previewImage.setImageBitmap(bitmap)

                    binding.cameraFocus.visibility = View.GONE
                    binding.cameraGalery.visibility = View.GONE
                    binding.cameraFlash.visibility = View.GONE
                    binding.cameraPress.visibility = View.GONE
                    binding.cameraBtn.visibility = View.VISIBLE

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

    private inner class MyAnalyzer: ImageAnalysis.Analyzer {
        override fun analyze(image: ImageProxy) {
            image.close()
        }
    }

    companion object {
        private const val TAG = "CameraFragment"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRES_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }

}