package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia14octt.api.RetrofitClient
import com.example.guia14octt.api.RemoteProductoCreate
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
            if (_productos.value.isEmpty()) {
                seedLocal()
            }
        }
    }

    private fun seedLocal() {
        val app = getApplication<Application>()
        val pkg = app.packageName
        fun rawUri(name: String): String? {
            val id = app.resources.getIdentifier(name, "raw", pkg)
            return if (id != 0) "android.resource://$pkg/$id" else null
        }
        val items = listOf(
            Producto(nombre = "Bicicleta Urbana", descripcion = "Cuadro aluminio, 7 velocidades.", precio = 259990.0, stock = 10, categoria = "Bicicletas", imagenUri = rawUri("urbana_azul_01")),
            Producto(nombre = "Bicicleta Montaña", descripcion = "Suspensión delantera, 21 velocidades.", precio = 349990.0, stock = 8, categoria = "Bicicletas", imagenUri = rawUri("montana_negra_01")),
            Producto(nombre = "Bicicleta Ruta", descripcion = "Cuadro carbono, gruposet 105.", precio = 799990.0, stock = 6, categoria = "Bicicletas"),
            Producto(nombre = "Bicicleta Gravel", descripcion = "Versátil para asfalto y tierra.", precio = 599990.0, stock = 7, categoria = "Bicicletas"),
            Producto(nombre = "Bicicleta Eléctrica", descripcion = "Motor asistido, batería 400Wh.", precio = 899990.0, stock = 5, categoria = "Bicicletas"),
            Producto(nombre = "Bicicleta Infantil", descripcion = "Ruedas 16\" con estabilizadores.", precio = 179990.0, stock = 12, categoria = "Bicicletas", imagenUri = rawUri("infantil_azul_01")),
            Producto(nombre = "Casco", descripcion = "Casco liviano con ventilación.", precio = 29990.0, stock = 30, categoria = "Accesorios", imagenUri = rawUri("casc_proteccion_01")),
            Producto(nombre = "Guantes", descripcion = "Guantes acolchados antideslizantes.", precio = 19990.0, stock = 25, categoria = "Accesorios"),
            Producto(nombre = "Luz LED", descripcion = "Set delantero y trasero recargable.", precio = 24990.0, stock = 40, categoria = "Accesorios", imagenUri = rawUri("luz_led_01")),
            Producto(nombre = "Candado", descripcion = "Candado U de alta seguridad.", precio = 34990.0, stock = 20, categoria = "Accesorios"),
            Producto(nombre = "Botella", descripcion = "600 ml libre de BPA.", precio = 9990.0, stock = 60, categoria = "Accesorios", imagenUri = rawUri("botella_agua_01")),
            Producto(nombre = "Bombín", descripcion = "Inflador portátil con manómetro.", precio = 14990.0, stock = 35, categoria = "Accesorios"),
            Producto(nombre = "Neumático 700x25c", descripcion = "Carretera, compuesto duradero.", precio = 29990.0, stock = 40, categoria = "Repuestos"),
            Producto(nombre = "Neumático 29\"", descripcion = "MTB, agarre superior.", precio = 34990.0, stock = 35, categoria = "Repuestos"),
            Producto(nombre = "Cámara 700c", descripcion = "Válvula Presta.", precio = 9990.0, stock = 80, categoria = "Repuestos"),
            Producto(nombre = "Pastillas de freno", descripcion = "Orgánicas para disco.", precio = 14990.0, stock = 50, categoria = "Repuestos"),
            Producto(nombre = "Cadena 9v", descripcion = "Resistente y silenciosa.", precio = 19990.0, stock = 40, categoria = "Repuestos"),
            Producto(nombre = "Piñón 11-28T", descripcion = "Compatible carretera 9v.", precio = 34990.0, stock = 25, categoria = "Repuestos"),
            Producto(nombre = "Maillot", descripcion = "Telas respirables, bolsillos traseros.", precio = 24990.0, stock = 25, categoria = "General"),
            Producto(nombre = "Pantalón térmico", descripcion = "Invierno, afelpado interior.", precio = 39990.0, stock = 20, categoria = "General"),
            Producto(nombre = "Chaqueta impermeable", descripcion = "Corta viento/lluvia.", precio = 69990.0, stock = 15, categoria = "General"),
            Producto(nombre = "Gafas ciclismo", descripcion = "Lentes fotocromáticos.", precio = 29990.0, stock = 30, categoria = "General"),
            Producto(nombre = "Cinta manillar", descripcion = "Gel confort.", precio = 14990.0, stock = 40, categoria = "General"),
            Producto(nombre = "Portabidón", descripcion = "Aluminio liviano.", precio = 12990.0, stock = 50, categoria = "General")
        )
        _productos.value = items
    }

    fun agregar(nombre: String, descripcion: String, precio: Double, stock: Int, categoria: String, imagenUri: String?) {
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
                    imagenUri = created.imagen ?: imagenUri
                )
                _productos.value = listOf(nuevo) + _productos.value
            } catch (_: Exception) {
                val id = (_productos.value.maxOfOrNull { it.id } ?: 0) + 1
                val nuevo = Producto(id = id, nombre = nombre, descripcion = descripcion, precio = precio, stock = stock, categoria = categoria, imagenUri = imagenUri)
                _productos.value = listOf(nuevo) + _productos.value
            }
        }
    }

    fun actualizar(id: Int, nombre: String, descripcion: String, precio: Double, stock: Int, categoria: String, imagenUri: String?) {
        _productos.value = _productos.value.map {
            if (it.id == id) it.copy(nombre = nombre, descripcion = descripcion, precio = precio, stock = stock, categoria = categoria, imagenUri = imagenUri) else it
        }
    }

    suspend fun getProducto(id: Int): Producto? = _productos.value.firstOrNull { it.id == id }

    fun limpiar() {
        seedLocal()
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
}
