package com.example.finalproject.entities

data class Post(
    val id: String = "", // מזהה פוסט, אפשר להשאיר ריק ולמשוך מה-Document
    val description: String = "", // תיאור הפוסט
    val userId: String = "", // מזהה המשתמש
    val imageUrl: String = "", // קישור לתמונה
    val timestamp: Long = 0L // חותמת זמן בפורמט מספרי
)

