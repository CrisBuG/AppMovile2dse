package com.example.guia14octt.model

data class Compra(
    val id: Int = 0,
    val productId: Int,
    val nombreProducto: String,
    val cantidad: Int,
    val total: Double,
    val fechaMillis: Long
)