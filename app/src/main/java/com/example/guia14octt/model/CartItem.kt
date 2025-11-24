package com.example.guia14octt.model

data class CartItem(
    val id: Int = 0,
    val productId: Int,
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Double
)