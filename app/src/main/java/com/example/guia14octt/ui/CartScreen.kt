package com.example.guia14octt.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
  onCheckoutDone: () -> Unit,
  cartViewModel: CartViewModel = viewModel(),
  authViewModel: AuthViewModel = viewModel()
) {
  val items by cartViewModel.items.collectAsState()
  val subtotal by cartViewModel.total.collectAsState()
  val envio = remember(subtotal, items) { if (items.isNotEmpty()) 2990.0 else 0.0 }
  val total = remember(subtotal, envio) { subtotal + envio }
  val usuario by authViewModel.usuarioActual.collectAsState()

  Scaffold(topBar = { TopAppBar(title = { Text("Carrito") }) }) { padding ->
    Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items, key = { it.id }) { item ->
          Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth()) {
              Text(item.nombre, style = MaterialTheme.typography.titleMedium)
              Spacer(Modifier.weight(1f))
              Text(formatClp(item.precioUnitario * item.cantidad))
            }
            Row(Modifier.fillMaxWidth()) {
              Text("Cantidad: ${item.cantidad}")
              Spacer(Modifier.width(12.dp))
              IconButton(onClick = { cartViewModel.decrease(item.id) }, enabled = item.cantidad > 1) { Icon(Icons.Filled.Remove, contentDescription = "Menos") }
              IconButton(onClick = { cartViewModel.increase(item.id) }, enabled = item.cantidad < item.stockMax) { Icon(Icons.Filled.Add, contentDescription = "Más") }
              Spacer(Modifier.weight(1f))
              IconButton(onClick = { cartViewModel.remove(item.id) }) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
            }
          }
        }
      }

      Row(Modifier.fillMaxWidth()) {
        Text("Subtotal")
        Spacer(Modifier.weight(1f))
        Text(formatClp(subtotal))
      }
      Row(Modifier.fillMaxWidth()) {
        Text("Envío")
        Spacer(Modifier.weight(1f))
        Text(formatClp(envio))
      }
      Row(Modifier.fillMaxWidth()) {
        Text("Total", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.weight(1f))
        Text(formatClp(total), style = MaterialTheme.typography.titleMedium)
      }

      Button(
        onClick = {
          val userId = (usuario?.id ?: 0).toLong()
          cartViewModel.checkoutCart(userId) { ok -> if (ok) onCheckoutDone() }
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = items.isNotEmpty()
      ) { Text("Pagar") }
    }
  }
}
