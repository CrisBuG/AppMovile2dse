package com.example.guia14octt.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBuyNow: (Int) -> Unit,
    onOpenQuienes: () -> Unit,
    onOpenDetail: (Int) -> Unit,
    productosViewModel: ProductosViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val productos by productosViewModel.productos.collectAsState()

    var query by remember { mutableStateOf("") }
    val categorias = remember(productos) { listOf("Todos") + productos.map { it.categoria }.distinct() }
    var categoriaSel by remember { mutableStateOf("Todos") }

    val destacados = remember(productos) { productos.filter { it.nombre.contains("Bicicleta", ignoreCase = true) }.take(3) }
    val listados = remember(productos, query, categoriaSel) {
        productos.filter { p ->
            (query.isBlank() || p.nombre.contains(query, ignoreCase = true) || p.descripcion.contains(query, ignoreCase = true)) &&
            (categoriaSel == "Todos" || p.categoria == categoriaSel)
        }.take(6)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Home") },
            actions = {
                IconButton(onClick = onOpenQuienes) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                brush = Brush.linearGradient(listOf(Color(0xFFFF9A8B), Color(0xFFFF6A88)))
                            )
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.DirectionsBike, contentDescription = "Quiénes Somos", tint = Color.White)
                    }
                }
            }
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Búsqueda
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar productos") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Sugerencias bajo el buscador
            if (query.isNotBlank()) {
                val sugerencias = remember(productos, query, categoriaSel) {
                    productos.filter { p ->
                        (p.nombre.contains(query, ignoreCase = true) || p.descripcion.contains(query, ignoreCase = true)) &&
                        (categoriaSel == "Todos" || p.categoria == categoriaSel)
                    }.take(5)
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sugerencias.forEach { p ->
                        Card(onClick = { onOpenDetail(p.id) }, shape = RoundedCornerShape(12.dp)) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                val uri = p.imagenUri?.let { android.net.Uri.parse(it) }
                                val id = if (uri != null && uri.scheme == "android.resource") uri.lastPathSegment?.toIntOrNull() else null
                                val type = if (id != null) LocalContext.current.resources.getResourceTypeName(id) else null
                                if (id != null && (type == "drawable" || type == "mipmap")) {
                                    Image(
                                        painter = painterResource(id),
                                        contentDescription = p.nombre,
                                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else if (p.imagenUri != null) {
                                    AsyncImage(
                                        model = p.imagenUri,
                                        contentDescription = p.nombre,
                                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE9ECEF)))
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(p.nombre, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                                    Text(formatClp(p.precio), style = MaterialTheme.typography.labelSmall)
                                }
                                AssistChip(onClick = { onOpenDetail(p.id) }, label = { Text("Ver") })
                            }
                        }
                    }
                }
            }

            // Categorías
            var expandedCat by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expandedCat, onExpandedChange = { expandedCat = !expandedCat }) {
                OutlinedTextField(
                    value = categoriaSel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandedCat, onDismissRequest = { expandedCat = false }) {
                    categorias.forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            categoriaSel = option
                            expandedCat = false
                        })
                    }
                }
            }


            // Descubre (dos tarjetas con gradiente)
            Text("Descubre", style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(96.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(listOf(Color(0xFFFF9A8B), Color(0xFFFF6A88)))
                        )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Ofertas", color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Explora nuestras Ofertas", color = Color.White, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(96.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(listOf(Color(0xFF56CCF2), Color(0xFF2F80ED)))
                        )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Nuevos", color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Explora nuestras Novedades", color = Color.White, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }


            Text("Productos destacados", style = MaterialTheme.typography.titleLarge)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().height(320.dp)
            ) {
                items(destacados) { p ->
                    Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                        Box {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                val uri = p.imagenUri?.let { android.net.Uri.parse(it) }
                                val id = if (uri != null && uri.scheme == "android.resource") uri.lastPathSegment?.toIntOrNull() else null
                                val type = if (id != null) LocalContext.current.resources.getResourceTypeName(id) else null
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
                                Text(p.nombre, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(formatClp(p.precio), style = MaterialTheme.typography.labelLarge)
                                Button(onClick = { cartViewModel.buyNow(p); onBuyNow(p.id) }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Comprar ahora")
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .offset(x = 12.dp, y = 12.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFD90429))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "12% Descuento", color = Color.White, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            // Listado breve
            Text("Tal vez te interese", style = MaterialTheme.typography.titleMedium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                items(listados) { p ->
                    Card(modifier = Modifier.width(240.dp)) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val uri = p.imagenUri?.let { android.net.Uri.parse(it) }
                            val id = if (uri != null && uri.scheme == "android.resource") uri.lastPathSegment?.toIntOrNull() else null
                            val type = if (id != null) LocalContext.current.resources.getResourceTypeName(id) else null
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
                            Text(p.nombre, style = MaterialTheme.typography.titleSmall)
                            Text(p.categoria, style = MaterialTheme.typography.bodySmall)
                            Text(formatClp(p.precio), style = MaterialTheme.typography.labelLarge)
                            OutlinedButton(onClick = { cartViewModel.addToCart(p) }, modifier = Modifier.fillMaxWidth()) { Text("Agregar al carrito") }
                        }
                    }
                }
            }
        }
    }
}