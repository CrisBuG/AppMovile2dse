package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia14octt.api.OrdersRetrofitClient
import com.example.guia14octt.api.PedidoItemRequest
import com.example.guia14octt.api.PedidoRequest
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
    val existing = _items.value.firstOrNull { it.productId == producto.id }
    if (existing != null) {
      _items.value = _items.value.map {
        if (it.productId == producto.id) {
          val nuevaCantidad = (it.cantidad + cantidad).coerceAtMost(it.stockMax)
          it.copy(cantidad = nuevaCantidad)
        } else it
      }
    } else {
      val nextId = (_items.value.maxOfOrNull { it.id } ?: 0) + 1
      val nuevo = CartItem(
        id = nextId,
        productId = producto.id,
        nombre = producto.nombre,
        cantidad = cantidad.coerceAtMost(producto.stock.coerceAtLeast(1)),
        precioUnitario = producto.precio,
        stockMax = producto.stock
      )
      _items.value = listOf(nuevo) + _items.value
    }
    recalc()
  }

  fun increase(id: Int) {
    _items.value = _items.value.map {
      if (it.id == id) it.copy(cantidad = (it.cantidad + 1).coerceAtMost(it.stockMax)) else it
    }
    recalc()
  }

  fun decrease(id: Int) {
    _items.value = _items.value
      .map { if (it.id == id) it.copy(cantidad = (it.cantidad - 1).coerceAtLeast(0)) else it }
      .filter { it.cantidad > 0 }
    recalc()
  }

  fun remove(id: Int) {
    _items.value = _items.value.filter { it.id != id }
    recalc()
  }

  fun buyNow(producto: Producto, cantidad: Int = 1) {
    addToCart(producto, cantidad)
  }

  fun checkoutCart(userId: Long, onResult: (Boolean) -> Unit = {}) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val itemsReq = items.value.map { i ->
          PedidoItemRequest(
            productId = i.productId.toLong(),
            cantidad = i.cantidad,
            precioUnitario = i.precioUnitario
          )
        }
        val resp = OrdersRetrofitClient.api.crear(
          PedidoRequest(userId = userId, items = itemsReq)
        )
        SalesStore.add(
          Compra(
            productId = -1,
            nombreProducto = "Pedido #${resp.id}",
            cantidad = items.value.sumOf { it.cantidad },
            total = resp.total,
            fechaMillis = System.currentTimeMillis()
          )
        )
        _items.value = emptyList()
        recalc()
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) { onResult(true) }
      } catch (_: Exception) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) { onResult(false) }
      }
    }
  }

  fun clearCart() {
    _items.value = emptyList()
    recalc()
  }
}
