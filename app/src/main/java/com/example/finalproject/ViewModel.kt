package com.example.finalproject

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.entities.BookItem
import com.example.finalproject.entities.Post
import com.example.finalproject.entities.UploadState
import com.example.finalproject.entities.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModel: ViewModel() {

    private val repository = AuthRepository()

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> get() = _authResult

//    private val _uploadState = MutableLiveData<UploadState>()
//    val uploadState: LiveData<UploadState> = _uploadState

    private val _books = MutableLiveData<List<BookItem>>()
    val books: LiveData<List<BookItem>> = _books

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> = _isUserLoggedIn

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _uploadState = MutableLiveData<UploadState>()
    val uploadState: LiveData<UploadState> get() = _uploadState

    private val _userPosts = MutableLiveData<List<Post>>()
    val userPosts: LiveData<List<Post>> = _userPosts

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts



    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginState.value = if (result.isSuccess) {
                LoginState.Success
            } else {
                LoginState.Error(result.exceptionOrNull()?.message ?: "שגיאה לא ידועה")
            }
        }
    }


    fun register(email: String, password: String) {
        viewModelScope.launch {
            val isSuccess = repository.register(email, password)
            _authResult.value = isSuccess
        }
    }

        fun uploadPost(description: String, imageUri: Uri) {
            _uploadState.value = UploadState.Loading

            repository.uploadPost(description, imageUri,
                onSuccess = {
                    _uploadState.value = UploadState.Success
                },
                onFailure = { errorMessage ->
                    _uploadState.value = UploadState.Error(errorMessage)
                }
            )
        }

    fun loadPosts() {
        repository.loadPosts(
            onSuccess = { postList ->
                _posts.value = postList
            },
            onFailure = { errorMessage ->
                _uploadState.value = UploadState.Error(errorMessage)
            }
        )
    }

//    fun loadUserPosts(userId: String) {
//        repository.getUserPosts(
//            userId = userId,
//            onResult = { postsList ->
//                _userPosts.postValue(postsList)
//            },
//            onError = { exception ->
//                _error.postValue(exception.message ?: "שגיאה בטעינת הפוסטים")
//            }
//        )
//    }


//    fun uploadPost(imageUri: Uri, description: String) {
//        _uploadState.value = UploadState.Loading
//
//        viewModelScope.launch {
//            try {
//                repository.uploadPost(imageUri, description)
//                _uploadState.value = UploadState.Success
//            } catch (e: Exception) {
//                _uploadState.value = UploadState.Error(e.message ?: "שגיאה לא ידועה")
//            }
//        }
//    }

    fun search(query: String) {
        _loading.value = true
        repository.searchBooks(query) { result ->
            _loading.postValue(false)
            result.onSuccess {
                _books.postValue(it.items)
            }.onFailure {
                _error.postValue(it.message)
            }
        }
    }

//    fun updateProfile(name: String, imageUri: Uri?) {
//        repository.updateUserProfile(name, imageUri) { success ->
//            _updateSuccess.postValue(success)
//        }
//    }

    fun updateProfile(name: String, imageUri: Uri?) {
        repository.updateUserProfile(name, imageUri) { success ->
            _updateSuccess.value = success
        }
    }

    fun getUserProfile(onResult: (UserProfile?) -> Unit) {
        repository.getUserProfile(onResult)
    }

//    fun updatePost(postId: String, newDescription: String, newImageUri: Uri?, onResult: (Boolean) -> Unit) {
//        repository.updatePost(postId, newDescription, newImageUri, onResult)
//    }


    fun checkUserLoggedIn() {
        val isLoggedIn = repository.isUserLoggedIn()
        Log.d("AuthCheck", "isUserLoggedIn = $isLoggedIn")
        _isUserLoggedIn.value = isLoggedIn
    }

    fun signOut(){
        repository.signOut()
        _isUserLoggedIn.value = false
    }

}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

