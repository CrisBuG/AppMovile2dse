package com.example.guia14octt.api

data class OrderItem(
    val productId: Int,
    val quantity: Int,
    val unitPrice: Double
)

data class CreateOrderRequest(
    val userId: Int,
    val items: List<OrderItem>
)

data class Order(
    val id: Int,
    val userId: Int,
    val total: Double,
    val items: List<OrderItem>,
    val createdAt: String
)