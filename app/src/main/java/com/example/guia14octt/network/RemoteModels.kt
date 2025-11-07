package com.example.guia14octt.network

// Modelo remoto basado en Fake Store API
data class RemoteProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)