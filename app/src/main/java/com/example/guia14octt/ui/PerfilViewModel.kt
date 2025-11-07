package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.guia14octt.data.AppDatabase
import com.example.guia14octt.data.Compra
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope

class PerfilViewModel(app: Application) : AndroidViewModel(app) {
    private val comprasDao = AppDatabase.getInstance(app).comprasDao()

    val compras: StateFlow<List<Compra>> = comprasDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}