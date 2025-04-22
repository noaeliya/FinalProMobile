package com.example.finalproject

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.entities.UploadState
import com.example.finalproject.fragments.AuthRepository
import kotlinx.coroutines.launch

class viewModel: ViewModel() {

    private val repository = AuthRepository()

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> get() = _authResult

    private val _uploadState = MutableLiveData<UploadState>()
    val uploadState: LiveData<UploadState> = _uploadState

    fun login(email: CharSequence, password: CharSequence) {
        viewModelScope.launch {
            val isSuccess = repository.login(this@viewModel.email.value ?: "", this@viewModel.password.value ?: "")
            _authResult.value = isSuccess
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val isSuccess = repository.register(email, password) // << כאן השינוי
            _authResult.value = isSuccess
        }
    }

    fun uploadPost(imageUri: Uri, description: String) {
        _uploadState.value = UploadState.Loading

        viewModelScope.launch {
            try {
                repository.uploadPost(imageUri, description)
                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "שגיאה לא ידועה")
            }
        }
    }

}