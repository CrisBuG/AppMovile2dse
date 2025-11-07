package com.example.guia14octt.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onSuccess: () -> Unit, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("user@bike.cl") }
    var pass by remember { mutableStateOf("1234") }
    var rememberMe by remember { mutableStateOf(true) }
    val loggedIn by authViewModel.loggedIn.collectAsState()

    LaunchedEffect(loggedIn) {
        if (loggedIn) onSuccess()
    }

    
    Surface(tonalElevation = 2.dp) { 
        val context = androidx.compose.ui.platform.LocalContext.current
        val imageLoader = remember(context) {
            coil.ImageLoader.Builder(context)
                .components { add(coil.decode.SvgDecoder.Factory()) }
                .build()
        }
        var targetAlpha by remember { mutableStateOf(0f) }
        val alphaAnim by androidx.compose.animation.core.animateFloatAsState(
            targetValue = targetAlpha,
            animationSpec = androidx.compose.animation.core.tween(durationMillis = 1200)
        )
        LaunchedEffect(Unit) { targetAlpha = 0.85f }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                coil.compose.AsyncImage(
                    model = com.example.guia14octt.R.raw.unnamed,
                    contentDescription = "Logo VELO-APP",
                    imageLoader = imageLoader,
                    modifier = Modifier.size(120.dp).alpha(alphaAnim)
                )
                Spacer(Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Iniciar sesion", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(checked = rememberMe, onCheckedChange = { rememberMe = it })
                            Spacer(Modifier.width(8.dp))
                            Text("Recordarme", style = MaterialTheme.typography.bodySmall)
                        }
                        TextButton(onClick = { /* TODO: flujo de recuperar */ }) {
                            Text("¿Olvidaste tu contraseña?")
                        }
                    }
                    Button(
                        onClick = { authViewModel.login(email, pass) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Ingresar") }
                    Text(
                        text = "Credenciales demo: user@bike.cl / 1234",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    }
                }
            }
        }
    }
}