package com.example.finalproject.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class VolumeInfo(
    val title: String?,
    val description: String?,
    val authors: List<String>?,
    val imageLinks: ImageLinks?
) : Parcelable