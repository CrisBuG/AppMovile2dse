package com.example.guia14octt.network


data class RemoteProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)