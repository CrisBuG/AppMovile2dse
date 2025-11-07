package com.example.guia14octt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductosDao {
    @Query("SELECT * FROM productos")
    fun getAll(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Producto?

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: Producto)

    @Query("DELETE FROM productos")
    suspend fun clear()

    @Query("UPDATE productos SET imagenUri = :uri WHERE nombre = :nombre")
    suspend fun setImagenUri(nombre: String, uri: String)

    @Query("SELECT * FROM productos WHERE nombre = :nombre LIMIT 1")
    suspend fun getByNombre(nombre: String): Producto?

    @Update
    suspend fun update(producto: Producto)


    @Query("UPDATE productos SET stock = stock - :cantidad WHERE id = :id AND stock >= :cantidad")
    suspend fun decrementStockIfAvailable(id: Int, cantidad: Int): Int

    @Query("UPDATE productos SET stock = stock + :cantidad WHERE id = :id")
    suspend fun incrementStock(id: Int, cantidad: Int): Int
}