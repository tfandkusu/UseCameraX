package com.tfandkusu.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {
    private val _uiModel = MutableLiveData(
        CameraUiModel(
            progress = false,
            takeEnabled = true,
            showPhoto = false
        )
    )

    val uiModel: LiveData<CameraUiModel> = _uiModel

    fun onTakePhoto() {
        _uiModel.value = requireNotNull(uiModel.value).copy(
            progress = true,
            takeEnabled = false
        )
    }

    fun onSavePhoto(srcFilePath: String, rotatedFilePath: String) = viewModelScope.launch(Dispatchers.IO) {
        // 右90度回転して保存する
        val srcBitmap = BitmapFactory.decodeFile(srcFilePath)
        val matrix = Matrix()
        matrix.postRotate(90f)
        val rotatedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.width, srcBitmap.height, matrix, true)
        rotatedBitmap.saveToFile(rotatedFilePath)
        _uiModel.postValue(
            requireNotNull(uiModel.value).copy(
                showPhoto = true
            )
        )
    }

    fun onShowPhotoResult() {
        _uiModel.value = requireNotNull(uiModel.value).copy(
            progress = false,
            takeEnabled = true,
            showPhoto = false
        )
    }

}

private fun Bitmap.saveToFile(filename: String) {
    val file = java.io.File(filename)
    val stream = java.io.FileOutputStream(file)
    this.compress(Bitmap.CompressFormat.JPEG, 95, stream)
    stream.flush()
    stream.close()
}
