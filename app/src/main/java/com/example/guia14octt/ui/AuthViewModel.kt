package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia14octt.api.AuthRetrofitClient
import com.example.guia14octt.api.LoginRequest
import com.example.guia14octt.api.RegisterRequest
import com.example.guia14octt.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
  private val prefs = app.getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)

  private val _usuarioActual = MutableStateFlow<User?>(null)
  val usuarioActual: StateFlow<User?> = _usuarioActual

  private val _loggedIn = MutableStateFlow(false)
  val loggedIn: StateFlow<Boolean> = _loggedIn

  private val _userName = MutableStateFlow("Usuario")
  val userName: StateFlow<String> = _userName

  fun login(correo: String, contrasena: String) { login(correo, contrasena, false) }

  fun login(correo: String, contrasena: String, remember: Boolean) {
    viewModelScope.launch {
      try {
        val auth = AuthRetrofitClient.api.login(LoginRequest(email = correo.trim(), password = contrasena))
        val u = User(
          id = auth.id.toInt(),
          firstName = auth.email.substringBefore('@'),
          lastName = "",
          email = auth.email,
          phone = null,
          photoUri = null
        )
        _usuarioActual.value = u
        _loggedIn.value = true
        _userName.value = u.firstName
        if (remember) prefs.edit().putString("remember_email", u.email).apply()
      } catch (_: Exception) { }
    }
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
      try {
        if (contrasena != confirmarContrasena) {
          onResult(Result.failure(IllegalArgumentException("Contraseñas no coinciden")))
          return@launch
        }
        val auth = AuthRetrofitClient.api.register(RegisterRequest(nombre = nombre.trim(), email = correo.trim(), password = contrasena))
        val u = User(
          id = auth.id.toInt(),
          firstName = nombre.trim(),
          lastName = apellido.trim(),
          email = auth.email,
          phone = telefono,
          photoUri = fotoUri
        )
        _usuarioActual.value = u
        _loggedIn.value = true
        _userName.value = u.firstName
        onResult(Result.success(u))
      } catch (e: Exception) {
        onResult(Result.failure(e))
      }
    }
  }

  fun logout() {
    _usuarioActual.value = null
    _loggedIn.value = false
    _userName.value = "Usuario"
    prefs.edit().remove("remember_email").apply()
  }

  fun loginGoogle(givenName: String?, familyName: String?, email: String?, photoUrl: String?, remember: Boolean = false, onResult: (Result<User>) -> Unit = {}) {
    viewModelScope.launch {
      try {
        val u = User(
          id = 0,
          firstName = (givenName ?: (email ?: "usuario").substringBefore('@')),
          lastName = (familyName ?: ""),
          email = (email ?: "usuario@example.com").trim().lowercase(),
          phone = null,
          photoUri = photoUrl
        )
        _usuarioActual.value = u
        _loggedIn.value = true
        _userName.value = u.firstName
        if (remember) prefs.edit().putString("remember_email", u.email).apply()
        onResult(Result.success(u))
      } catch (e: Exception) {
        onResult(Result.failure(e))
      }
    }
  }

  fun actualizarFoto(uri: String?) {
    val current = _usuarioActual.value
    if (current != null) {
      _usuarioActual.value = current.copy(photoUri = uri)
    }
  }
}
