package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia14octt.api.RemoteProductoCreate
import com.example.guia14octt.api.RetrofitClient
import com.example.guia14octt.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductosViewModel(app: Application) : AndroidViewModel(app) {
  private val _productos = MutableStateFlow<List<Producto>>(emptyList())
  val productos: StateFlow<List<Producto>> = _productos

  init {
    viewModelScope.launch(Dispatchers.IO) {
      syncFromApi()
    }
  }

  fun syncFromApi() {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val remote = RetrofitClient.api.listar()
        val mapped = remote.map {
          Producto(
            id = it.id,
            nombre = it.nombre,
            descripcion = it.descripcion ?: "",
            precio = it.precio,
            stock = it.stock ?: 0,
            categoria = it.categoria ?: "General",
            imagenUri = it.imagen
          )
        }
        _productos.value = mapped
      } catch (_: Exception) { }
    }
  }

  fun agregar(
    nombre: String,
    descripcion: String,
    precio: Double,
    stock: Int,
    categoria: String,
    imagenUri: String?
  ) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val created = RetrofitClient.api.crear(
          RemoteProductoCreate(
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            stock = stock,
            categoria = categoria,
            imagen = imagenUri
          )
        )
        val nuevo = Producto(
          id = created.id,
          nombre = created.nombre,
          descripcion = created.descripcion ?: descripcion,
          precio = created.precio,
          stock = created.stock ?: stock,
          categoria = created.categoria ?: categoria,
          imagenUri = created.imagen
        )
        _productos.value = listOf(nuevo) + _productos.value
      } catch (_: Exception) { }
    }
  }

  fun actualizar(
    id: Int,
    nombre: String,
    descripcion: String,
    precio: Double,
    stock: Int,
    categoria: String,
    imagenUri: String?
  ) {
    viewModelScope.launch(Dispatchers.IO) {
      _productos.value = _productos.value.map {
        if (it.id == id) it.copy(
          nombre = nombre,
          descripcion = descripcion,
          precio = precio,
          stock = stock,
          categoria = categoria,
          imagenUri = imagenUri
        ) else it
      }
    }
  }

  fun eliminar(id: Int) {
    viewModelScope.launch(Dispatchers.IO) {
      _productos.value = _productos.value.filter { it.id != id }
    }
  }

  fun getProducto(id: Int): Producto? = _productos.value.firstOrNull { it.id == id }

  fun limpiar() {
    syncFromApi()
  }
}
