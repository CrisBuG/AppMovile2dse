package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia14octt.model.CartItem
import com.example.guia14octt.model.Compra
import com.example.guia14octt.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(app: Application) : AndroidViewModel(app) {
    private val _items: MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    private val _total: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    private fun recalc() {
        _total.value = _items.value.sumOf { it.precioUnitario * it.cantidad }
    }

    fun addToCart(producto: Producto, cantidad: Int = 1) {
        val nextId = (_items.value.maxOfOrNull { it.id } ?: 0) + 1
        val nuevo = CartItem(id = nextId, productId = producto.id, nombre = producto.nombre, cantidad = cantidad, precioUnitario = producto.precio)
        _items.value = listOf(nuevo) + _items.value
        recalc()
    }

    fun increase(id: Int) {
        _items.value = _items.value.map { if (it.id == id) it.copy(cantidad = it.cantidad + 1) else it }
        recalc()
    }

    fun decrease(id: Int) {
        _items.value = _items.value
            .map { if (it.id == id) it.copy(cantidad = it.cantidad - 1) else it }
            .filter { it.cantidad > 0 }
        recalc()
    }

    fun remove(id: Int) {
        _items.value = _items.value.filter { it.id != id }
        recalc()
    }

    fun buyNow(producto: Producto, cantidad: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            SalesStore.add(
                Compra(
                    productId = producto.id,
                    nombreProducto = producto.nombre,
                    cantidad = cantidad,
                    total = producto.precio * cantidad,
                    fechaMillis = System.currentTimeMillis()
                )
            )
            _items.value = emptyList()
            recalc()
        }
    }

    fun checkoutCart() {
        viewModelScope.launch(Dispatchers.IO) {
            items.value.forEach { item ->
                SalesStore.add(
                    Compra(
                        productId = item.productId,
                        nombreProducto = item.nombre,
                        cantidad = item.cantidad,
                        total = item.precioUnitario * item.cantidad,
                        fechaMillis = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun clearCart() {
        _items.value = emptyList()
        recalc()
    }
}
