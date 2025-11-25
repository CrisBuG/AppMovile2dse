package com.example.guia14octt.api

data class PedidoItemRequest(
    val productId: Long,
    val cantidad: Int,
    val precioUnitario: Double
)

data class PedidoRequest(
    val userId: Long,
    val items: List<PedidoItemRequest>
)

data class PedidoItemResponse(
    val id: Long,
    val pedidoId: Long,
    val productId: Long,
    val cantidad: Int,
    val precioUnitario: Double
)

data class PedidoResponse(
    val id: Long,
    val userId: Long,
    val total: Double,
    val createdAt: String,
    val items: List<PedidoItemResponse>
)
