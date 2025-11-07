package com.example.guia14octt.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Producto::class, CartItem::class, Compra::class, User::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productosDao(): ProductosDao
    abstract fun cartDao(): CartDao
    abstract fun comprasDao(): ComprasDao
    abstract fun usersDao(): UsersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "productos.db"
                ).fallbackToDestructiveMigration()
                 .build().also { INSTANCE = it }
            }
        }
    }
}