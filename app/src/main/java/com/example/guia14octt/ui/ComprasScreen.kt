package com.example.guia14octt.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComprasScreen(perfilViewModel: PerfilViewModel = viewModel()) {
    val compras by perfilViewModel.compras.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Compras") }) }) { padding ->
        if (compras.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Sin compras aún")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(compras, key = { it.id }) { c ->
                    Card { 
                        Column(Modifier.padding(12.dp)) {
                            Text(c.nombreProducto, style = MaterialTheme.typography.titleSmall)
                            Text("x${c.cantidad}", style = MaterialTheme.typography.bodySmall)
                            Text(formatClp(c.total), style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}