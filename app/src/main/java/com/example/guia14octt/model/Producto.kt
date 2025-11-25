package com.example.guia14octt.model

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val imagenUri: String? = null
)