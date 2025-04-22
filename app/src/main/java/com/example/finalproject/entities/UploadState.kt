package com.example.finalproject.entities

sealed class UploadState {
    object Loading : UploadState()
    object Success : UploadState()
    class Error(val message: String) : UploadState()
}
