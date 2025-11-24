package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.guia14octt.model.Compra
import kotlinx.coroutines.flow.StateFlow

class PerfilViewModel(app: Application) : AndroidViewModel(app) {
    val compras: StateFlow<List<Compra>> = SalesStore.compras
}