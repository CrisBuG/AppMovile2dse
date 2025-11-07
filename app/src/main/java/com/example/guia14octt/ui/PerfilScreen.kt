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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    onVerCompras: () -> Unit,
    onAgregarProducto: () -> Unit,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val userName by authViewModel.userName.collectAsState()
    var showEdit by remember { mutableStateOf(false) }
    var nameEdit by remember(userName) { mutableStateOf(userName) }

    Scaffold(topBar = { TopAppBar(title = { Text("Profile") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(
                    Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Hola, $userName", style = MaterialTheme.typography.titleLarge)
                    Text("¿Qué deseas hacer hoy?", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Botón principal con gradiente: Ver compras realizadas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFFFF6F61), Color(0xFFD32F2F))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onVerCompras() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ver compras realizadas",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }


            OutlinedButton(onClick = onAgregarProducto, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Agregar producto")
            }

            OutlinedButton(onClick = { showEdit = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Editar mis datos")
            }


            Button(
                onClick = {
                    authViewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Cerrar Sesión")
            }
        }
    }

    if (showEdit) {
        AlertDialog(
            onDismissRequest = { showEdit = false },
            title = { Text("Editar mis datos") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nameEdit,
                        onValueChange = { nameEdit = it },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Solo se edita el nombre visible.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    authViewModel.updateUserName(nameEdit.trim().ifBlank { userName })
                    showEdit = false
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showEdit = false }) { Text("Cancelar") }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    MaterialTheme {
        PerfilScreen(
            onVerCompras = {},
            onAgregarProducto = {},
            onLogout = {}
        )
    }
}