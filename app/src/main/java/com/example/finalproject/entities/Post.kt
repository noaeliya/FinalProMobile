package com.example.finalproject.entities

data class Post(
    val description: String = "",
    val imageBase64: String = "",
    val userId: String = "",
    val timestamp: com.google.firebase.Timestamp? = null
)
