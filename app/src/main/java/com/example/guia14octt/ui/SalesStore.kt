package com.example.guia14octt.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.guia14octt.model.Compra

object SalesStore {
    private val _compras = MutableStateFlow<List<Compra>>(emptyList())
    val compras: StateFlow<List<Compra>> = _compras

    fun add(compra: Compra) {
        val nextId = (_compras.value.maxOfOrNull { it.id } ?: 0) + 1
        _compras.value = listOf(compra.copy(id = nextId)) + _compras.value
    }

    fun clear() { _compras.value = emptyList() }
    fun remove(id: Int) { _compras.value = _compras.value.filter { it.id != id } }
}