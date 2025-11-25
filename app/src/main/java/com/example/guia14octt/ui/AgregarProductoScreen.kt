package com.example.guia14octt.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MenuAnchorType
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(
    onCancel: () -> Unit,
    onSaved: () -> Unit,
    productId: Int? = null,
    productosViewModel: ProductosViewModel = viewModel()
) {

    val productos by productosViewModel.productos.collectAsState()
    val existing = remember(productId, productos) { productos.firstOrNull { it.id == productId } }

    var nombre by remember(existing) { mutableStateOf(existing?.nombre ?: "") }
    var descripcion by remember(existing) { mutableStateOf(existing?.descripcion ?: "") }
    var precioText by remember(existing) { mutableStateOf(existing?.precio?.toString() ?: "") }
    var stockText by remember(existing) { mutableStateOf((existing?.stock ?: 0).toString()) }

    val categorias = listOf("Bicicletas", "Accesorios", "Repuestos", "General")
    var categoria by remember(existing) { mutableStateOf(existing?.categoria ?: categorias.first()) }

    var imagenUri by remember(existing) { mutableStateOf<Uri?>(existing?.imagenUri?.let { Uri.parse(it) }) }
    val context = LocalContext.current
    var recursoNombre by remember { mutableStateOf("") }
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) { }
            val localUri = copyImageToAppStorage(context, uri)
            imagenUri = localUri ?: uri
        }
    }


    val precioInvalido = precioText.isNotEmpty() && precioText.toDoubleOrNull() == null
    val puedeGuardar = nombre.isNotBlank() && !precioInvalido && precioText.toDoubleOrNull() != null

    val isEditing = existing != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar producto" else "Agregar producto") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancelar")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val precio = precioText.toDoubleOrNull()
                        val stock = stockText.toIntOrNull() ?: 0
                        if (nombre.isNotBlank() && precio != null) {
                            val finalImagenUri = if (recursoNombre.isNotBlank()) {
                                val resId = context.resources.getIdentifier(recursoNombre, "raw", context.packageName)
                                if (resId != 0) android.net.Uri.parse("android.resource://${context.packageName}/$resId").toString() else imagenUri?.toString()
                            } else imagenUri?.toString()
                            if (isEditing) {
                                productosViewModel.actualizar(
                                    id = existing!!.id,
                                    nombre = nombre.trim(),
                                    descripcion = descripcion.trim(),
                                    precio = precio,
                                    stock = stock,
                                    categoria = categoria,
                                    imagenUri = finalImagenUri
                                )
                            } else {
                                productosViewModel.agregar(
                                    nombre = nombre.trim(),
                                    descripcion = descripcion.trim(),
                                    precio = precio,
                                    stock = stock,
                                    categoria = categoria,
                                    imagenUri = finalImagenUri
                                )
                            }
                            onSaved()
                        }
                    }, enabled = puedeGuardar) { Text(if (isEditing) "Guardar cambios" else "Guardar") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = { if (nombre.isBlank()) Text("Campo obligatorio") }
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = precioText,
                onValueChange = { precioText = it },
                label = { Text("Precio *") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = precioInvalido,
                supportingText = { if (precioInvalido) Text("Ingresa un número válido") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = stockText,
                onValueChange = { stockText = it },
                label = { Text("Stock") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Categoría (Dropdown)
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = { categoria = cat; expanded = false }
                        )
                    }
                }
            }


            Text("Imágenes", style = MaterialTheme.typography.titleMedium)
            Card(shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = recursoNombre,
                        onValueChange = { recursoNombre = it.filter { ch -> ch.isLowerCase() || ch.isDigit() || ch == '_' } },
                        label = { Text("Nombre recurso (res/raw)") },
                        placeholder = { Text("ej: prod_bicicleta_01") },
                        supportingText = { Text("Coloca archivos en res/raw y escribe su nombre (sin extensión)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val previewModel = remember(recursoNombre, imagenUri) {
                        if (recursoNombre.isNotBlank()) {
                            val resId = context.resources.getIdentifier(recursoNombre, "raw", context.packageName)
                            if (resId != 0) android.net.Uri.parse("android.resource://${context.packageName}/$resId") else imagenUri
                        } else imagenUri
                    }

                    if (previewModel != null) {
                        AsyncImage(
                            model = previewModel,
                            contentDescription = "Vista previa",
                            modifier = Modifier.fillMaxWidth().height(160.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                            Text("Sin imagen seleccionada", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { pickImage.launch(arrayOf("image/*")) }, modifier = Modifier.weight(1f)) {
                            Text("Seleccionar imagen")
                        }
                        OutlinedButton(onClick = { imagenUri = null; recursoNombre = "" }, modifier = Modifier.weight(1f), enabled = (imagenUri != null || recursoNombre.isNotBlank())) {
                            Text("Quitar imagen")
                        }
                    }
                }
            }
        }
    }
}


private fun copyImageToAppStorage(context: android.content.Context, source: android.net.Uri): android.net.Uri? {
    return try {
        val resolver = context.contentResolver
        val mime = resolver.getType(source) ?: "image/jpeg"
        val ext = when (mime) {
            "image/png" -> ".png"
            "image/webp" -> ".webp"
            else -> ".jpg"
        }
        val imagesDir = java.io.File(context.filesDir, "images")
        if (!imagesDir.exists()) imagesDir.mkdirs()
        val file = java.io.File(imagesDir, "img_${System.currentTimeMillis()}$ext")
        resolver.openInputStream(source)?.use { input ->
            java.io.FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        android.net.Uri.fromFile(file)
    } catch (_: Exception) {
        null
    }
}

