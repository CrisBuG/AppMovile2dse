package com.example.guia14octt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): User?

    @Update
    suspend fun update(user: User)

    @Query("UPDATE users SET photoUri = :uri WHERE id = :id")
    suspend fun setPhotoUri(id: Int, uri: String?)
}