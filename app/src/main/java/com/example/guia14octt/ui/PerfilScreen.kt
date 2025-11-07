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
import coil.compose.AsyncImage
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    onVerCompras: () -> Unit,
    onAgregarProducto: () -> Unit,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val usuario by authViewModel.usuarioActual.collectAsState()
    val userName by authViewModel.userName.collectAsState()

    val pickPhotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        authViewModel.actualizarFoto(uri?.toString())
    }

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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = usuario?.photoUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(96.dp)
                    )
                    Text("Hola, ${userName}", style = MaterialTheme.typography.titleLarge)
                    if (usuario != null) {
                        Text("Correo: ${usuario!!.email}", style = MaterialTheme.typography.bodyMedium)
                        if (!usuario!!.phone.isNullOrBlank()) {
                            Text("Fono: ${usuario!!.phone}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    OutlinedButton(onClick = { pickPhotoLauncher.launch("image/*") }) { Text("Cambiar foto") }
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

    // Edición de nombre removida para simplificar; ahora se muestran datos reales y foto.
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