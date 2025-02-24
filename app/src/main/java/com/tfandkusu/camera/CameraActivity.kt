package com.tfandkusu.camera

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import com.tfandkusu.camera.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageCapture: ImageCapture
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
                binding.take.setOnClickListener {
                    takePhoto()
                }
            } catch (_: Exception) {
                showCameraErrorDialog(R.string.camera_error_start_message)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                File(
                    filesDir.path + "/captured.jpg",
                )
            )
            .build()
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
        val intent = Intent(this, ShowPhotoActivity::class.java)
        startActivity(intent)
    }
}