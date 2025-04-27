package com.example.finalproject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.WindowInsetsAnimation
import com.example.finalproject.entities.BookResponse
import com.example.finalproject.entities.MyPost
import com.example.finalproject.entities.Post
import com.example.finalproject.entities.UserProfile
import com.example.finalproject.http.NetworkManager
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
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


    fun uploadPost(
        description: String,
        imageUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val storageReference =
            FirebaseStorage.getInstance().reference.child("post_images/${UUID.randomUUID()}")

        // העלאת התמונה ל-Storage
        storageReference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    val post = hashMapOf(
                        "userId" to firebaseAuth.currentUser?.uid,
                        "description" to description,
                        "imageUri" to downloadUri.toString(),
                        "timestamp" to System.currentTimeMillis()
                    )

                    firestore.collection("posts")
                        .add(post)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onFailure(e.message ?: "Unknown error")
                        }
                }.addOnFailureListener { e ->
                    onFailure(e.message ?: "Failed to retrieve download URL")
                }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Failed to upload image")
            }
    }


    fun loadPosts(onSuccess: (List<Post>) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val posts = querySnapshot.documents.mapNotNull { doc ->
                    val description = doc.getString("description") ?: return@mapNotNull null
                    val imageUri = doc.getString("imageUri") ?: return@mapNotNull null
                    val timestamp = doc.getLong("timestamp") ?: return@mapNotNull null

                    Post(
                        id = doc.id,
                        description = description,
                        imageUrl = imageUri,
                        timestamp = timestamp
                    )
                }
                onSuccess(posts)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Unknown error")
            }
    }

    fun searchBooks(query: String, callback: (Result<BookResponse>) -> Unit) {
        NetworkManager.getApi()
            .searchBooks(query, 20, NetworkManager.API_KEY)
            .enqueue(object : retrofit2.Callback<BookResponse> {

                override fun onResponse(
                    call: retrofit2.Call<BookResponse>,
                    response: retrofit2.Response<BookResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
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

    fun updateUserProfile(name: String, imageUri: Uri?, onResult: (Boolean) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid ?: return
        val userRef = firestore.collection("users").document(uid)

        val updateMap = hashMapOf<String, Any>(
            "fullName" to name
        )

        if (imageUri != null) {
            val localPath = saveImageLocally(App.context, imageUri, uid)
            if (localPath != null) {
                updateMap["profileImageLocalPath"] = localPath
            }
        }

        userRef.set(updateMap, SetOptions.merge())
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getUserProfile(onResult: (UserProfile?) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profileImageLocalPath = document.getString("profileImageLocalPath")
                    val fullName = document.getString("fullName")
                    onResult(UserProfile(fullName ?: "", profileImageLocalPath ?: ""))
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }


    private fun saveImageLocally(context: Context, imageUri: Uri, filename: String): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val file = File(context.filesDir, "$filename.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getUserPosts(userId: String, onSuccess: (List<MyPost>) -> Unit, onFailure: (String) -> Unit) {
        Log.d("Firestore", "Fetching posts for user: $userId")  // לוג כדי לדעת מתי התחלנו לחפש

        val postsRef = FirebaseFirestore.getInstance().collection("posts")
        postsRef.whereEqualTo("userId", userId).get()
            .addOnSuccessListener { documents ->
                Log.d("Firestore", "Found ${documents.size()} posts")  // לוג כמות הפוסטים שנמצאו

                val posts = mutableListOf<MyPost>()
                for (document in documents) {
                    val post = document.toObject(MyPost::class.java)
                    val imageUrl = document.getString("imageUrl")  // שליפת ה-URL בצורה ישירה
                    Log.d("Firestore", "Fetched post with imageUrl: $imageUrl")
                    posts.add(post)
                }
                onSuccess(posts)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching posts: ${exception.message}")  // לוג שגיאה אם משהו לא עובד
                onFailure(exception.message ?: "Error fetching posts")
            }
    }




//    fun getUserPosts(userId: String, onResult: (List<MyPost>) -> Unit, onError: (Exception) -> Unit) {
//        firestore.collection("posts")
//            .whereEqualTo("userId", userId)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                val postList = querySnapshot.documents.mapNotNull { document ->
//                    Log.d("noaaaaa", document.toString()+""+userId.toString())
//                    document.toObject(MyPost::class.java)
//                }
//                onResult(postList)
//            }
//
//            .addOnFailureListener { exception ->
//                onError(exception)
//            }
//    }


    fun isUserLoggedIn(): Boolean {
        Log.d("firebaseAuth", (firebaseAuth.currentUser != null).toString())
        return firebaseAuth.currentUser != null
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}


