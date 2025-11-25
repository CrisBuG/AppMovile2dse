package com.example.guia14octt.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthUser

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthUser

    @GET("auth/me")
    suspend fun me(@Query("userId") userId: Long): AuthUser
}
