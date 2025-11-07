package com.example.guia14octt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia14octt.data.AppDatabase
import com.example.guia14octt.data.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductosViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.getInstance(app).productosDao()

    val productos: StateFlow<List<Producto>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) { seedIfEmpty(); patchResourceImages() }
    }

    private suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            val app = getApplication<Application>()
            val pkg = app.packageName
            fun rawUri(name: String): String? {
                val id = app.resources.getIdentifier(name, "raw", pkg)
                return if (id != 0) "android.resource://$pkg/$id" else null
            }
            val items = listOf(
                // Bicicletas (6)
                Producto(nombre = "Bicicleta Urbana", descripcion = "Cuadro aluminio, 7 velocidades.", precio = 259990.0, stock = 10, categoria = "Bicicletas", imagenUri = rawUri("urbana_azul_01")),
                Producto(nombre = "Bicicleta Montaña", descripcion = "Suspensión delantera, 21 velocidades.", precio = 349990.0, stock = 8, categoria = "Bicicletas", imagenUri = rawUri("montana_negra_01")),
                Producto(nombre = "Bicicleta Ruta", descripcion = "Cuadro carbono, gruposet 105.", precio = 799990.0, stock = 6, categoria = "Bicicletas"),
                Producto(nombre = "Bicicleta Gravel", descripcion = "Versátil para asfalto y tierra.", precio = 599990.0, stock = 7, categoria = "Bicicletas"),
                Producto(nombre = "Bicicleta Eléctrica", descripcion = "Motor asistido, batería 400Wh.", precio = 899990.0, stock = 5, categoria = "Bicicletas"),
                Producto(nombre = "Bicicleta Infantil", descripcion = "Ruedas 16\" con estabilizadores.", precio = 179990.0, stock = 12, categoria = "Bicicletas", imagenUri = rawUri("infantil_azul_01")),

                // Accesorios (6)
                Producto(nombre = "Casco", descripcion = "Casco liviano con ventilación.", precio = 29990.0, stock = 30, categoria = "Accesorios", imagenUri = rawUri("casc_proteccion_01")),
                Producto(nombre = "Guantes", descripcion = "Guantes acolchados antideslizantes.", precio = 19990.0, stock = 25, categoria = "Accesorios"),
                Producto(nombre = "Luz LED", descripcion = "Set delantero y trasero recargable.", precio = 24990.0, stock = 40, categoria = "Accesorios", imagenUri = rawUri("luz_led_01")),
                Producto(nombre = "Candado", descripcion = "Candado U de alta seguridad.", precio = 34990.0, stock = 20, categoria = "Accesorios"),
                Producto(nombre = "Botella", descripcion = "600 ml libre de BPA.", precio = 9990.0, stock = 60, categoria = "Accesorios", imagenUri = rawUri("botella_agua_01")),
                Producto(nombre = "Bombín", descripcion = "Inflador portátil con manómetro.", precio = 14990.0, stock = 35, categoria = "Accesorios"),

                // Repuestos (6)
                Producto(nombre = "Neumático 700x25c", descripcion = "Carretera, compuesto duradero.", precio = 29990.0, stock = 40, categoria = "Repuestos"),
                Producto(nombre = "Neumático 29\"", descripcion = "MTB, agarre superior.", precio = 34990.0, stock = 35, categoria = "Repuestos"),
                Producto(nombre = "Cámara 700c", descripcion = "Válvula Presta.", precio = 9990.0, stock = 80, categoria = "Repuestos"),
                Producto(nombre = "Pastillas de freno", descripcion = "Orgánicas para disco.", precio = 14990.0, stock = 50, categoria = "Repuestos"),
                Producto(nombre = "Cadena 9v", descripcion = "Resistente y silenciosa.", precio = 19990.0, stock = 40, categoria = "Repuestos"),
                Producto(nombre = "Piñón 11-28T", descripcion = "Compatible carretera 9v.", precio = 34990.0, stock = 25, categoria = "Repuestos"),

                // General (6)
                Producto(nombre = "Maillot", descripcion = "Telas respirables, bolsillos traseros.", precio = 24990.0, stock = 25, categoria = "General"),
                Producto(nombre = "Pantalón térmico", descripcion = "Invierno, afelpado interior.", precio = 39990.0, stock = 20, categoria = "General"),
                Producto(nombre = "Chaqueta impermeable", descripcion = "Corta viento/lluvia.", precio = 69990.0, stock = 15, categoria = "General"),
                Producto(nombre = "Gafas ciclismo", descripcion = "Lentes fotocromáticos.", precio = 29990.0, stock = 30, categoria = "General"),
                Producto(nombre = "Cinta manillar", descripcion = "Gel confort.", precio = 14990.0, stock = 40, categoria = "General"),
                Producto(nombre = "Portabidón", descripcion = "Aluminio liviano.", precio = 12990.0, stock = 50, categoria = "General")
            )
            items.forEach { dao.insert(it) }
        }
    }

    private suspend fun patchResourceImages() {
        val app = getApplication<Application>()
        val pkg = app.packageName
        fun rawUri(name: String): String? {
            val id = app.resources.getIdentifier(name, "raw", pkg)
            return if (id != 0) "android.resource://$pkg/$id" else null
        }

        val mapping = listOf(
            "Bicicleta Urbana" to "urbana_azul_01",
            "Bicicleta Montaña" to "montana_negra_01",
            "Bicicleta Infantil" to "infantil_azul_01",
            "Casco" to "casc_proteccion_01",
            "Luz LED" to "luz_led_01"
        )
        mapping.forEach { (nombre, rawName) ->
            val uri = rawUri(rawName) ?: return@forEach
            dao.setImagenUri(nombre, uri)
        }
    }

    fun agregar(nombre: String, descripcion: String, precio: Double, stock: Int, categoria: String, imagenUri: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(
                Producto(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio,
                    stock = stock,
                    categoria = categoria,
                    imagenUri = imagenUri
                )
            )
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
            val existente = dao.getById(id)
            if (existente != null) {
                val actualizado = existente.copy(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio,
                    stock = stock,
                    categoria = categoria,
                    imagenUri = imagenUri
                )
                dao.update(actualizado)
            }
        }
    }

    suspend fun getProducto(id: Int): Producto? = dao.getById(id)

    fun limpiar() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clear()
            seedIfEmpty()
        }
    }
}