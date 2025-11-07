package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia14octt.data.AppDatabase
import com.example.guia14octt.data.AuthRepository
import com.example.guia14octt.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AuthRepository(AppDatabase.getInstance(app).usersDao())
    private val prefs = app.getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)

    private val _usuarioActual = MutableStateFlow<User?>(null)
    val usuarioActual: StateFlow<User?> = _usuarioActual

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn: StateFlow<Boolean> = _loggedIn

    private val _userName = MutableStateFlow("Usuario")
    val userName: StateFlow<String> = _userName

    init {
        // Mantener userName y loggedIn sincronizados con usuarioActual
        viewModelScope.launch {
            usuarioActual.collect { u ->
                _loggedIn.value = (u != null)
                _userName.value = if (u != null) listOfNotNull(u.firstName, u.lastName).joinToString(" ") else "Usuario"
            }
        }

        // Cargar sesión recordada si existe
        viewModelScope.launch {
            val rememberedEmail = prefs.getString("remember_email", null)
            if (rememberedEmail != null) {
                val u = repo.getByEmail(rememberedEmail)
                _usuarioActual.value = u
            }
        }
    }

    fun login(correo: String, contrasena: String, remember: Boolean, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val u = repo.login(correo, contrasena)
            _usuarioActual.value = u
            // Persistir si Recordarme está activo; limpiar si no
            prefs.edit().apply {
                if (remember && u != null) putString("remember_email", u.email) else remove("remember_email")
            }.apply()
            onResult(u != null)
        }
    }

    // Conveniencia: login con flag remember sin callback
    fun login(correo: String, contrasena: String, remember: Boolean) {
        login(correo, contrasena, remember) { }
    }

    // Sobrecarga para compatibilidad con pantallas existentes
    fun login(correo: String, contrasena: String) {
        login(correo, contrasena, false) { }
    }

    fun registrar(
        nombre: String,
        apellido: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String,
        telefono: String?,
        fotoUri: String?,
        onResult: (Result<User>) -> Unit
    ) {
        viewModelScope.launch {
            if (contrasena != confirmarContrasena) {
                onResult(Result.failure(IllegalArgumentException("Las contraseñas no coinciden")))
                return@launch
            }
            val r = repo.register(nombre, apellido, correo, contrasena, telefono, fotoUri)
            onResult(r)
        }
    }

    fun loginGoogle(email: String, givenName: String?, familyName: String?, photoUrl: String?, remember: Boolean = false) {
        viewModelScope.launch {
            val u = repo.loginOrCreateFromGoogle(email, givenName, familyName)
            _usuarioActual.value = if (photoUrl != null) u.copy(photoUri = photoUrl) else u
            if (remember) prefs.edit().putString("remember_email", u.email).apply()
        }
    }

    fun actualizarFoto(uri: String?) {
        viewModelScope.launch {
            _usuarioActual.value?.let { u ->
                repo.updatePhoto(u.id, uri)
                _usuarioActual.value = u.copy(photoUri = uri)
            }
        }
    }

    fun logout() {
        _usuarioActual.value = null
        prefs.edit().remove("remember_email").apply()
    }
}