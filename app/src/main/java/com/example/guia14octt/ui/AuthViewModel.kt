package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val _loggedIn = MutableStateFlow(false)
    val loggedIn: StateFlow<Boolean> = _loggedIn

    private val _userName = MutableStateFlow("Usuario")
    val userName: StateFlow<String> = _userName

    // Credenciales fijas
    private val validEmail = "user@bike.cl"
    private val validPass = "1234"

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            val ok = (email.trim() == validEmail && pass == validPass)
            _loggedIn.value = ok
            if (ok) {
                _userName.value = email.substringBefore('@').replaceFirstChar { it.uppercaseChar() }
            }
        }
    }

    fun logout() { 
        _loggedIn.value = false
        _userName.value = "Usuario"
    }


    fun updateUserName(name: String) {
        _userName.value = name.ifBlank { "Usuario" }
    }
}