package com.example.guia14octt.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OrdersRetrofitClient {
    private const val BASE_URL = "http://192.168.1.12:8082/"

    val api: OrdersService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrdersService::class.java)
    }
}
