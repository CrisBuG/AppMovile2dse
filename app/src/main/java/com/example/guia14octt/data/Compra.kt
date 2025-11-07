package com.example.guia14octt.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compras")
data class Compra(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val nombreProducto: String,
    val cantidad: Int,
    val total: Double,
    val fechaMillis: Long
)