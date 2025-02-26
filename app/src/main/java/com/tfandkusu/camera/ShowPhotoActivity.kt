package com.tfandkusu.camera

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import coil3.imageLoader
import coil3.load
import com.tfandkusu.camera.databinding.ActivityShowPhotoBinding
import java.io.File

class ShowPhotoActivity : AppCompatActivity() {

    lateinit var binding: ActivityShowPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShowPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageLoader.memoryCache?.clear()
        imageLoader.diskCache?.clear()
        binding.imageView.load(
            Uri.fromFile(
                File(
                    filesDir.path + "/" + CameraActivity.FILE_NAME_CAPTURED,
                )
            )
        )
    }
}