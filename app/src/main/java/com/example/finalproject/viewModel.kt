package com.example.finalproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.fragments.AuthRepository
import kotlinx.coroutines.launch

class viewModel: ViewModel() {
    private val repository = AuthRepository()

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> get() = _authResult

    fun login() {
        viewModelScope.launch {
            val isSuccess = repository.login(email.value ?: "", password.value ?: "")
            _authResult.value = isSuccess
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val isSuccess = repository.register(email, password) // << כאן השינוי
            _authResult.value = isSuccess
        }
    }

}