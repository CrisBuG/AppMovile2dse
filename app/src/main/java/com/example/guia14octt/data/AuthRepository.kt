package com.example.guia14octt.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class AuthRepository(private val usersDao: UsersDao) {

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String?,
        photoUri: String?
    ): Result<User> = withContext(Dispatchers.IO) {
        val existing = usersDao.getByEmail(email)
        if (existing != null) return@withContext Result.failure(IllegalStateException("Email ya registrado"))
        val hashed = hash(password)
        val user = User(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = email.trim().lowercase(),
            passwordHash = hashed,
            phone = phone?.trim(),
            photoUri = photoUri
        )
        usersDao.insert(user)
        val created = usersDao.getByEmail(email)!!
        Result.success(created)
    }

    suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        val u = usersDao.getByEmail(email.trim().lowercase())
        if (u != null && u.passwordHash == hash(password)) u else null
    }

    suspend fun loginOrCreateFromGoogle(email: String, givenName: String?, familyName: String?): User = withContext(Dispatchers.IO) {
        val norm = email.trim().lowercase()
        val existing = usersDao.getByEmail(norm)
        if (existing != null) return@withContext existing
        val user = User(
            firstName = (givenName ?: "Usuario"),
            lastName = (familyName ?: ""),
            email = norm,
            passwordHash = null,
            phone = null,
            photoUri = null
        )
        usersDao.insert(user)
        usersDao.getByEmail(norm)!!
    }

    suspend fun updatePhoto(userId: Int, uri: String?) = withContext(Dispatchers.IO) {
        usersDao.setPhotoUri(userId, uri)
    }

    private fun hash(text: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(text.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}