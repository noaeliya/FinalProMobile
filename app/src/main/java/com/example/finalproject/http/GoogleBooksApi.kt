package com.example.finalproject.http

import com.example.finalproject.entities.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call


interface GoogleBooksApi {
    @GET("volumes")
    fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResult: Int,
        @Query("key") apiKey: String
    ): retrofit2.Call<BookResponse>
}
