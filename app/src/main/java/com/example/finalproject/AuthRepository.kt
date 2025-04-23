package com.example.finalproject

import android.net.Uri
import android.view.WindowInsetsAnimation
import com.example.finalproject.entities.BookResponse
import com.example.finalproject.http.NetworkManager
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import retrofit2.Call

class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance().reference
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Boolean {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun register(email: String, password: String): Boolean {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun uploadPost(imageUri: Uri, description: String) {
        val fileName = "posts/${System.currentTimeMillis()}.jpg"
        val imageRef = storage.child(fileName)

        val uploadTask = imageRef.putFile(imageUri).await()
        val downloadUrl = imageRef.downloadUrl.await().toString()

        val post = mapOf(
            "description" to description,
            "imageUrl" to downloadUrl,
            "timestamp" to FieldValue.serverTimestamp()
        )

        firestore.collection("book_posts").add(post).await()
    }

    fun searchBooks(query: String, callback: (Result<BookResponse>) -> Unit) {
        NetworkManager.getApi()
            .searchBooks(query, 20, NetworkManager.API_KEY)
            .enqueue(object : retrofit2.Callback<BookResponse> {

                override fun onResponse(
                    call: retrofit2.Call<BookResponse>,
                    response: retrofit2.Response<BookResponse>
                ) {
                    if (response.isSuccessful && response.body() != null){
                        callback(Result.success(response.body()!!))
                    } else {
                        callback(Result.failure(Throwable("לא נמצאו תוצאות")))
                    }
                }

                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    callback(Result.failure(t))
                }
            })
    }
}
