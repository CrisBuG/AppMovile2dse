package com.example.guia14octt.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("register")
    suspend fun register(@Body body: RegisterRequest): RegisterResponse

    @GET("me")
    suspend fun me(@Header("Authorization") bearer: String): RemoteUser
}