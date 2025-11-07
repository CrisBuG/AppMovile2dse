package com.example.guia14octt.data

import com.example.guia14octt.network.RetrofitClient

class ProductsRepository(private val dao: ProductosDao) {
    private val api = RetrofitClient.api

    /**
     * Descarga productos desde la API y los inserta en Room.
     * Retorna la cantidad insertada (0 si falla o no hay datos).
     */
    suspend fun refreshFromApi(): Int {
        return try {
            val remote = api.getProducts()
            var count = 0
            remote.forEach { r ->
                val producto = Producto(
                    id = 0, // autogenerado por Room
                    nombre = r.title,
                    descripcion = r.description,
                    precio = r.price,
                    stock = 10, // stock base demostrativo
                    categoria = r.category.ifBlank { "General" },
                    imagenUri = r.image
                )
                dao.insert(producto)
                count++
            }
            count
        } catch (_: Exception) {
            0
        }
    }
}