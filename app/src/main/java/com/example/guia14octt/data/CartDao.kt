package com.example.guia14octt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM carrito_items")
    fun getAll(): Flow<List<CartItem>>

    @Insert
    suspend fun insert(item: CartItem)

    @Query("UPDATE carrito_items SET cantidad = :cantidad WHERE id = :id")
    suspend fun updateCantidad(id: Int, cantidad: Int)

    @Query("DELETE FROM carrito_items")
    suspend fun clear()

    @Query("DELETE FROM carrito_items WHERE id = :id")
    suspend fun deleteById(id: Int)
}