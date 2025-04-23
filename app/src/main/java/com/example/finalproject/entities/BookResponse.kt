package com.example.finalproject.entities

import java.io.Serializable

data class BookResponse(
    val items: List<BookItem>?
) : Serializable
