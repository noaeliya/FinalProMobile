package com.example.finalproject.entities

import java.io.Serializable

data class VolumeInfo(
    val title: String?,
    val description: String?,
    val authors: List<String>?,
    val imageLinks: ImageLinks?
) : Serializable