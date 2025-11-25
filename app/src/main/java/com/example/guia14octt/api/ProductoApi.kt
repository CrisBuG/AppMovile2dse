package com.example.guia14octt.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductoApi {
    @GET("productos")
    suspend fun listar(): List<RemoteProducto>

    @POST("productos")
    suspend fun crear(@Body body: RemoteProductoCreate): RemoteProducto
}