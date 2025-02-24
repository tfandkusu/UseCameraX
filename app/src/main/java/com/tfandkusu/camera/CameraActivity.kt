package com.tfandkusu.camera

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.tfandkusu.camera.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageCapture: ImageCapture

    private val showPhotoResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        readyTakingPhoto()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.take.translationX = -systemBars.right.toFloat()
            insets
        }
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = binding.viewFinder.surfaceProvider
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            imageCapture = ImageCapture.Builder()
                .build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
                readyTakingPhoto()
            } catch (_: Exception) {
                showCameraErrorDialog(R.string.camera_error_start_message)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * 写真を撮影する UI 状態にする
     */
    private fun readyTakingPhoto() {
        binding.take.setOnClickListener {
            takePhoto()
        }
        binding.progress.isVisible = false
    }

    /**
     * 撮影した写真を処理する
     */
    private fun processPhoto() {

    }

    private fun takePhoto() {
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                File(
                    filesDir.path + "/captured.jpg",
                )
            )
            .build()
        binding.take.isClickable = false
        binding.progress.isVisible = true
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    callShowPhotoActivity()
                }

                override fun onError(e: ImageCaptureException) {
                    Log.d("TryCamera", "onError: ${e.message}", e)
                    showCameraErrorDialog(R.string.camera_error_take_message)
                }
            }
        )
    }

    private fun showCameraErrorDialog(
        @StringRes messageStringResId: Int
    ) {
        AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(messageStringResId)
            .setPositiveButton(R.string.ok) { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun callShowPhotoActivity() {
        binding.progress.isVisible = false
        val intent = Intent(this, ShowPhotoActivity::class.java)
        showPhotoResultLauncher.launch(intent)
    }
}