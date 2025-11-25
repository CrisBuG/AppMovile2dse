package com.example.guia14octt.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrdersService {
    @GET("pedidos/user/{userId}")
    suspend fun listar(@Path("userId") userId: Long): List<PedidoResponse>

    @POST("pedidos")
    suspend fun crear(@Body body: PedidoRequest): PedidoResponse
}
