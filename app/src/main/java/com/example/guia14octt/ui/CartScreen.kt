package com.example.guia14octt.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.animation.animateContentSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(onCheckoutDone: () -> Unit, cartViewModel: CartViewModel = viewModel()) {
    val items by cartViewModel.items.collectAsState()
    val subtotal by cartViewModel.total.collectAsState()
    val envio = remember(subtotal, items) { if (items.isNotEmpty()) 2990.0 else 0.0 }
    val total = remember(subtotal, envio) { subtotal + envio }

    Scaffold(topBar = { TopAppBar(title = { Text("Carrito") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    Card { 
                        Column(Modifier.padding(12.dp)) {
                            Text(item.nombre, style = MaterialTheme.typography.titleSmall)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { cartViewModel.decrease(item.id) }) { Text("-") }
                                Text("${item.cantidad}", style = MaterialTheme.typography.bodyMedium)
                                OutlinedButton(onClick = { cartViewModel.increase(item.id) }) { Text("+") }
                                Spacer(Modifier.width(8.dp))
                                Text(formatClp(item.precioUnitario * item.cantidad), style = MaterialTheme.typography.labelLarge)
                                Spacer(Modifier.weight(1f))
                                TextButton(onClick = { cartViewModel.remove(item.id) }) { Text("Eliminar") }
                            }
                        }
                    }
                }
            }
            Surface( tonalElevation = 2.dp ) {
                Column(Modifier.fillMaxWidth().padding(16.dp).animateContentSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
                        Text(formatClp(subtotal), style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Envío", style = MaterialTheme.typography.bodyMedium)
                        Text(formatClp(envio), style = MaterialTheme.typography.bodyMedium)
                    }
                    HorizontalDivider()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", style = MaterialTheme.typography.titleMedium)
                        Text(formatClp(total), style = MaterialTheme.typography.titleMedium)
                    }
                    Button(onClick = {
                        cartViewModel.checkoutCart()
                        onCheckoutDone()
                    }, modifier = Modifier.fillMaxWidth(), enabled = items.isNotEmpty()) {
                        Text("Pagar")
                    }
                }
            }
        }
    }
}