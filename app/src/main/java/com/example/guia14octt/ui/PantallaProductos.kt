package com.example.guia14octt.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaProductos(
    onOpen: (Int) -> Unit,
    onAgregar: () -> Unit,
    onBuyNow: () -> Unit,
    productosViewModel: ProductosViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val productos by productosViewModel.productos.collectAsState()

    var query by remember { mutableStateOf("") }
    val categoriasDisponibles = remember(productos) { listOf("Todos") + productos.map { it.categoria }.distinct() }
    var categoriaSel by remember { mutableStateOf("Todos") }

    val opcionesOrden = listOf("Precio asc", "Precio desc")
    var ordenSel by remember { mutableStateOf(opcionesOrden.first()) }

    val visibles = remember(productos, query, categoriaSel, ordenSel) {
        productos
            .filter { p ->
                (query.isBlank() || p.nombre.contains(query, ignoreCase = true) || p.descripcion.contains(query, ignoreCase = true)) &&
                (categoriaSel == "Todos" || p.categoria == categoriaSel)
            }
            .let { list ->
                when (ordenSel) {
                    "Precio desc" -> list.sortedByDescending { it.precio }
                    else -> list.sortedBy { it.precio }
                }
            }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Productos") }, actions = { TextButton(onClick = onAgregar) { Text("Agregar") } }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onAgregar) { Text("Agregar +") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar en todos los productos...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                // Categoría
                var expandedCat by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expandedCat, onExpandedChange = { expandedCat = !expandedCat }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = categoriaSel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedCat, onDismissRequest = { expandedCat = false }) {
                        categoriasDisponibles.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = {
                                categoriaSel = option
                                expandedCat = false
                            })
                        }
                    }
                }

                // Orden
                var expandedOrd by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expandedOrd, onExpandedChange = { expandedOrd = !expandedOrd }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = ordenSel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Orden") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOrd) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedOrd, onDismissRequest = { expandedOrd = false }) {
                        opcionesOrden.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = {
                                ordenSel = option
                                expandedOrd = false
                            })
                        }
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 260.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(visibles, key = { it.id }) { p ->
                    val percent = when {
                        p.nombre.contains("Urbana", ignoreCase = true) -> 13
                        p.nombre.contains("Montaña", ignoreCase = true) -> 12
                        else -> 12
                    }
                    val original = (p.precio / (1 - percent / 100.0))

                    Card(shape = RoundedCornerShape(16.dp)) {
                        Box {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (p.imagenUri != null) {
                                    val uri = if (p.imagenUri != null) android.net.Uri.parse(p.imagenUri) else null
                                    val id = if (uri != null && uri.scheme == "android.resource") uri.lastPathSegment?.toIntOrNull() else null
                                    val ctx = LocalContext.current
                                    val type = if (id != null) ctx.resources.getResourceTypeName(id) else null
                                    if (id != null && (type == "drawable" || type == "mipmap")) {
                                        Image(
                                            painter = painterResource(id),
                                            contentDescription = p.nombre,
                                            modifier = Modifier.fillMaxWidth().height(120.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else if (p.imagenUri != null) {
                                        AsyncImage(
                                            model = p.imagenUri,
                                            contentDescription = p.nombre,
                                            modifier = Modifier.fillMaxWidth().height(120.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(120.dp)
                                                .background(Color(0xFFE9ECEF))
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFE9ECEF))
                                    )
                                }
                                Text(p.nombre, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(p.descripcion, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = formatClp(original), style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough), color = Color.Gray)
                                    Text(text = formatClp(p.precio), style = MaterialTheme.typography.titleMedium, color = Color(0xFFD90429))
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                    OutlinedButton(onClick = { onOpen(p.id) }, modifier = Modifier.weight(1f)) { Text("Ver", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                                    Button(onClick = { cartViewModel.buyNow(p); onBuyNow() }, modifier = Modifier.weight(1f)) { Text("Comprar ahora", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .offset(x = 12.dp, y = 12.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFD90429))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "$percent% Descuento", color = Color.White, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}