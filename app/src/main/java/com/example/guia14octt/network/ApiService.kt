package com.example.guia14octt.network

import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<RemoteProduct>
}