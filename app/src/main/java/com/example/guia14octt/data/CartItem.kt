package com.example.guia14octt.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Double
)