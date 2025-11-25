package com.example.guia14octt.api

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)

data class AuthUser(
    val id: Long,
    val nombre: String,
    val email: String
)
