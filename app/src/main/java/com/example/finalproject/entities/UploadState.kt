package com.example.finalproject.entities

sealed class UploadState {
    object Success : UploadState()
    object Loading : UploadState()
    data class Error(val message: String) : UploadState()
    object Idle : UploadState()
}
