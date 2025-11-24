package com.example.guia14octt.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OrdersService {
    @GET("user/{id}")
    suspend fun getOrders(@Header("Authorization") bearer: String?, @Path("id") userId: Int): List<Order>

    @POST("")
    suspend fun create(@Header("Authorization") bearer: String?, @Body body: CreateOrderRequest): Order
}