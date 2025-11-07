package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia14octt.data.AppDatabase
import com.example.guia14octt.data.CartItem
import com.example.guia14octt.data.Compra
import com.example.guia14octt.data.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)
    private val cartDao = db.cartDao()
    private val comprasDao = db.comprasDao()
    private val productosDao = db.productosDao()

    val items: StateFlow<List<CartItem>> = cartDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val total: StateFlow<Double> = cartDao.getAll().map { list ->
        list.sumOf { it.precioUnitario * it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addToCart(producto: Producto, cantidad: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.insert(
                CartItem(
                    productId = producto.id,
                    nombre = producto.nombre,
                    cantidad = cantidad,
                    precioUnitario = producto.precio
                )
            )
        }
    }

    fun increase(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = items.value.firstOrNull { it.id == id } ?: return@launch
            cartDao.updateCantidad(id, item.cantidad + 1)
        }
    }

    fun decrease(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = items.value.firstOrNull { it.id == id } ?: return@launch
            val newQty = item.cantidad - 1
            if (newQty <= 0) cartDao.deleteById(id) else cartDao.updateCantidad(id, newQty)
        }
    }

    fun remove(id: Int) {
        viewModelScope.launch(Dispatchers.IO) { cartDao.deleteById(id) }
    }

    fun buyNow(producto: Producto, cantidad: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {

            val updated = productosDao.decrementStockIfAvailable(producto.id, cantidad)
            if (updated > 0) {
                val total = producto.precio * cantidad
                comprasDao.insert(
                    Compra(
                        productId = producto.id,
                        nombreProducto = producto.nombre,
                        cantidad = cantidad,
                        total = total,
                        fechaMillis = System.currentTimeMillis()
                    )
                )
            }

        }
    }

    fun checkoutCart() {
        viewModelScope.launch(Dispatchers.IO) {
            val current = items.value
            current.forEach { item ->

                val updated = productosDao.decrementStockIfAvailable(item.productId, item.cantidad)
                if (updated > 0) {
                    comprasDao.insert(
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
            cartDao.clear()
        }
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) { cartDao.clear() }
    }
}