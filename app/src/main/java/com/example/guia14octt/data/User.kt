package com.example.guia14octt.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val passwordHash: String? = null,
    val phone: String? = null,
    val photoUri: String? = null
)