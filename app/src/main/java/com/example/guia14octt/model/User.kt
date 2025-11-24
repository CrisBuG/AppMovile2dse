package com.example.guia14octt.model

data class User(
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String? = null,
    val photoUri: String? = null
)