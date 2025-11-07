package com.example.guia14octt.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Message
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.remember
import android.graphics.BitmapFactory
import androidx.compose.ui.layout.ContentScale
import com.example.guia14octt.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuienesSomosScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiénes Somos") },
                navigationIcon = {
                    TextButton(onClick = {  }) { Text("Volver a Home") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Intro con icono grande
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    val context = LocalContext.current
                    val logoBitmap = remember {
                        context.resources.openRawResource(R.raw.unnamed).use { ins ->
                            BitmapFactory.decodeStream(ins).asImageBitmap()
                        }
                    }
                    Image(
                        bitmap = logoBitmap,
                        contentDescription = "Logo de la app",
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Misión y Visión", style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFD64E42)))
                        Text("Misión: Poner al mundo sobre dos ruedas, ofreciendo productos de calidad con un diseño innovador.", style = MaterialTheme.typography.bodyMedium)
                        Text("Visión: Ser la tienda líder en movilidad urbana sostenible, inspirando un estilo de vida saludable y conectado con el medio ambiente.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Card(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Nuestra Historia", style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFD64E42)))
                    Text(
                        "Fundada en 2024, nuestra tienda nació de la pasión por el ciclismo y el diseño. Comenzamos en un pequeño taller, restaurando bicicletas clásicas con un toque moderno. Hoy, hemos crecido para ofrecer una selección curada de las mejores bicicletas y accesorios, manteniendo siempre nuestro espíritu original.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Nuestro Equipo", style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFD64E42)))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.Settings, contentDescription = null)
                            Text("Cristobal Bueno", style = MaterialTheme.typography.titleSmall)
                            Text("Fundador y CEO", style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.AccountBox, contentDescription = null)
                            Text("Cristobal Bueno", style = MaterialTheme.typography.titleSmall)
                            Text("Jefe de Taller", style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.Person, contentDescription = null)
                            Text("Cristobal Bueno", style = MaterialTheme.typography.titleSmall)
                            Text("Diseño y Marketing", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Contacto", style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFD64E42)))
                    Text("¿Tienes preguntas? ¡Contáctanos!", style = MaterialTheme.typography.bodyMedium)
                    Text("Email: contacto@tienda.cl", style = MaterialTheme.typography.bodyMedium)
                    Text("Teléfono: +56 9 1234 5678", style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                         Icon(Icons.Filled.CameraAlt, contentDescription = "Instagram")
                         Icon(Icons.Filled.Favorite, contentDescription = "Facebook")
                         Icon(Icons.Filled.Message, contentDescription = "WhatsApp")
                     }
                }
            }
        }
    }
}