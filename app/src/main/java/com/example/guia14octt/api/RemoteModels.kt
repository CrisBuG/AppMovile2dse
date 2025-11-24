package com.example.guia14octt.api

data class RemoteProducto(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val stock: Int?,
    val categoria: String?,
    val imagen: String?
)

data class RemoteProductoCreate(
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val stock: Int?,
    val categoria: String?,
    val imagen: String?
)