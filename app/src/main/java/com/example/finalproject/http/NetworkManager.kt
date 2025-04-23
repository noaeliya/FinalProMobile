package com.example.finalproject.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object NetworkManager {
    const val API_KEY = "AIzaSyAaDjbF4uLjEMK53gVaeOoY0isxyrRmBAw"
//    const val API_KEY = "AIzaSyAcJst4JfkSw6FfcljZ5gQt00pToYPt7q0"
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApi(): GoogleBooksApi = retrofit.create(GoogleBooksApi::class.java)
}