package com.tfandkusu.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    fun onSavePhoto() {
        _uiModel.value = requireNotNull(uiModel.value).copy(
            showPhoto = true
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
