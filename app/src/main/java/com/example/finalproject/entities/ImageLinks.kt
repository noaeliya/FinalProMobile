package com.example.finalproject.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageLinks(
    val thumbnail: String?
) : Parcelable
