package com.example.guia14octt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ComprasDao {
    @Query("SELECT * FROM compras ORDER BY fechaMillis DESC")
    fun getAll(): Flow<List<Compra>>

    @Insert
    suspend fun insert(compra: Compra)

    @Query("DELETE FROM compras")
    suspend fun clear()
}