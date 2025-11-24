package com.example.guia14octt.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    onBuyNow: () -> Unit,
    onAddToCart: () -> Unit,
    onEdit: (Int) -> Unit,
    productosViewModel: ProductosViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val productos by productosViewModel.productos.collectAsState()
    val producto = productos.firstOrNull { it.id == productId }

    Scaffold(topBar = { TopAppBar(title = { Text("Detalle del Producto") }) }) { padding ->
        if (producto == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Producto no encontrado")
            }
        } else {

            val imageUris = remember(producto) {
                val uri = producto.imagenUri
                if (uri != null) listOf(uri, uri, uri) else emptyList()
            }
            var selectedImage by remember(imageUris) { mutableStateOf(imageUris.firstOrNull()) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Galería
                Card(shape = RoundedCornerShape(16.dp)) {
                    if (selectedImage != null) {
                        val resId = remember(selectedImage) {
                            try {
                                val uri = android.net.Uri.parse(selectedImage)
                                if (uri.scheme == "android.resource") uri.lastPathSegment?.toIntOrNull() else null
                            } catch (_: Exception) { null }
                        }
                        if (resId != null) {
                            val type = LocalContext.current.resources.getResourceTypeName(resId)
                            if (type == "drawable" || type == "mipmap") {
                                Image(
                                    painter = painterResource(resId),
                                    contentDescription = producto.nombre,
                                    modifier = Modifier.fillMaxWidth().height(220.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                AsyncImage(
                                    model = selectedImage,
                                    contentDescription = producto.nombre,
                                    modifier = Modifier.fillMaxWidth().height(220.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        } else {
                            AsyncImage(
                                model = selectedImage,
                                contentDescription = producto.nombre,
                                modifier = Modifier.fillMaxWidth().height(220.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(180.dp),
                            contentAlignment = Alignment.Center
                        ) { Text("Sin imagen", style = MaterialTheme.typography.bodyMedium) }
                    }
                }
                if (imageUris.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        imageUris.forEachIndexed { idx, uri ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                                modifier = Modifier.size(64.dp).clickable { selectedImage = uri }
                            ) {
                                val thumbResId = remember(uri) {
                                    try {
                                        val parsed = android.net.Uri.parse(uri)
                                        if (parsed.scheme == "android.resource") parsed.lastPathSegment?.toIntOrNull() else null
                                    } catch (_: Exception) { null }
                                }
                                if (thumbResId != null) {
                                    val type = LocalContext.current.resources.getResourceTypeName(thumbResId)
                                    if (type == "drawable" || type == "mipmap") {
                                        Image(
                                            painter = painterResource(thumbResId),
                                            contentDescription = "Miniatura ${idx + 1}",
                                            modifier = Modifier.size(64.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        AsyncImage(
                                            model = uri,
                                            contentDescription = "Miniatura ${idx + 1}",
                                            modifier = Modifier.size(64.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                } else {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = "Miniatura ${idx + 1}",
                                        modifier = Modifier.size(64.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }


                Text(producto.nombre, style = MaterialTheme.typography.titleLarge)

                // Precios y stock
                val discount = 0.133 // ~13.3% para simular caso del mock
                val originalPrice = remember(producto.precio) { (producto.precio / (1 - discount)) }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatClp(originalPrice),
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textDecoration = TextDecoration.LineThrough)
                    )
                    Text(
                        text = formatClp(producto.precio),
                        style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                    )
                }

                val stockText = if (producto.stock > 0) "En Stock (${producto.stock} unidades)" else "Sin Stock"
                val stockColor = if (producto.stock > 0) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                Text(stockText, style = MaterialTheme.typography.titleSmall.copy(color = stockColor))

                val canBuy = producto.stock > 0

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(Color(0xFFFF6F61), Color(0xFFD32F2F))),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .let { base -> if (canBuy) base.clickable { cartViewModel.buyNow(producto); onBuyNow() } else base },
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (canBuy) "Comprar ahora" else "Sin stock", color = Color.White, style = MaterialTheme.typography.titleSmall)
                }


                OutlinedButton(
                    onClick = { cartViewModel.addToCart(producto); onAddToCart() },
                    enabled = canBuy,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Añadir al carrito")
                }


                OutlinedButton(
                    onClick = { onEdit(producto.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Editar")
                }


                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Descripción", style = MaterialTheme.typography.titleMedium)
                        Text(producto.descripcion.ifBlank { "Sin descripción" }, style = MaterialTheme.typography.bodyMedium)
                    }
                }


                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Especificaciones", style = MaterialTheme.typography.titleMedium)
                        SpecRow(label = "Categoría", value = producto.categoria)
                        SpecRow(label = "Stock", value = producto.stock.toString())
                        SpecRow(label = "ID", value = producto.id.toString())
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("$label:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}