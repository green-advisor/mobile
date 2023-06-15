package com.riski.greenadvisor.ui.camera

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.riski.greenadvisor.BetaActivity
import com.riski.greenadvisor.R
import com.riski.greenadvisor.databinding.ActivityCameraDeseasseBinding
import com.riski.greenadvisor.utils.uriToFile
import java.io.File

class CameraDeseasseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraDeseasseBinding
    private lateinit var previewView: PreviewView
    private lateinit var camera: Camera
    private var captureImg: ImageCapture? = null
    private var isPreviewShown = false
    private var isFlashOn = false
    private var getFile: File? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCameraDeseasseBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)
        supportActionBar?.hide()
        startCamera()
        cameraChange()

        previewView = binding.cameraPreview
        analysisFun()
        flashFun()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.cameraUpdateInfo.visibility = View.GONE
        }, 1500L)

        binding.cameraPress.setOnClickListener { takeImg() }
        binding.cameraGalery.setOnClickListener { startGallery() }
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

    private fun analysisFun() {
        binding.cameraBtn.setOnClickListener {
            startActivity(Intent(this, BetaActivity::class.java))
        }
    }

    private fun cameraChange() {
        binding.cameraChange2.setOnClickListener {
            val fragment = CameraFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(androidx.fragment.R.id.fragment_container_view_tag, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun flashFun() {
        binding.cameraFlash.setOnClickListener {
            if (isFlashOn) {
                turnFlashOff()
            } else {
                turnFlashOn()
            }
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
            val myFile = uriToFile(imgSelect, this@CameraDeseasseActivity)
            getFile = myFile
            binding.previewImage.setImageURI(imgSelect)
            binding.cameraBtn.visibility = View.VISIBLE

            isPreviewShown = true
        }
    }

    private fun turnFlashOn() {
        val cameraFlash = camera.cameraControl
        val cameraInfo = camera.cameraInfo
        if (cameraInfo.hasFlashUnit()) {
            cameraFlash.enableTorch(true)
            binding.cameraFlash.setImageResource(R.drawable.ic_baseline_flash_on_24)
            isFlashOn = true
        }
    }

    private fun turnFlashOff() {
        val cameraFlash = camera.cameraControl
        cameraFlash.enableTorch(false)
        binding.cameraFlash.setImageResource(R.drawable.ic_baseline_flash_off_24)
        isFlashOn = false
    }

    private fun startCamera()  {
        val cameraProviderfuture = ProcessCameraProvider.getInstance(this)

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
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takeImg() {
        val imageCapture = captureImg?: return
        val photoFile = File(
            this.externalMediaDirs.firstOrNull(), "${System.currentTimeMillis()}.jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    Toast.makeText(this@CameraDeseasseActivity,"Image Saved: $savedUri", Toast.LENGTH_SHORT).show()

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
                    Toast.makeText(this@CameraDeseasseActivity, "Image Error: ${exception.message}", Toast.LENGTH_SHORT).show()
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
}