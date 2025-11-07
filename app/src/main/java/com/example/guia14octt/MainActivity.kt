package com.example.guia14octt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.guia14octt.ui.*
import androidx.compose.foundation.layout.padding
import com.example.guia14octt.ui.theme.BikeShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BikeShopTheme {
                BikeShopApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeShopApp() {
    val nav = rememberNavController()
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val loggedIn by authViewModel.loggedIn.collectAsState()

    // Si hay sesión recordada, saltar login automáticamente
    androidx.compose.runtime.LaunchedEffect(loggedIn, currentRoute) {
        if (loggedIn && currentRoute == "login") {
            nav.navigate("home") { popUpTo("login") { inclusive = true } }
        }
    }

    val showBottomBar = currentRoute != "login" && currentRoute != "register"

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar { 
                    val items = listOf(
                        "home" to Icons.Outlined.Home,
                        "productos" to Icons.AutoMirrored.Outlined.List,
                        "carrito" to Icons.Outlined.ShoppingCart,
                        "perfil" to Icons.Outlined.Person
                    )
                    items.forEach { (route, icon) ->
                        val selected = isDestOn(nav, route)
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                nav.navigate(route) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(icon, contentDescription = route) },
                            label = { Text(route.replaceFirstChar { it.uppercaseChar() }) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = "login", modifier = Modifier.padding(padding)) {
            composable("login") {
                LoginScreen(
                    onSuccess = {
                        nav.navigate("home") { popUpTo("login") { inclusive = true } }
                    },
                    onOpenRegister = { nav.navigate("register") }
                )
            }
            composable("register") {
                RegisterScreen(
                    onSuccess = {
                        nav.navigate("login") { popUpTo("register") { inclusive = true } }
                    }
                )
            }
            composable("home") { HomeScreen(onBuyNow = { id -> nav.navigate("compras") }, onOpenQuienes = { nav.navigate("quienes") }, onOpenDetail = { id -> nav.navigate("detalle/$id") }) }
            composable("productos") { PantallaProductos(onOpen = { id -> nav.navigate("detalle/$id") }, onAgregar = { nav.navigate("agregar") }, onBuyNow = { nav.navigate("compras") }) }
            composable("carrito") { CartScreen(onCheckoutDone = { nav.navigate("perfil") }) }
            composable("perfil") { PerfilScreen(onVerCompras = { nav.navigate("compras") }, onAgregarProducto = { nav.navigate("agregar") }, onLogout = { nav.navigate("login") { popUpTo("login") { inclusive = true } } }) }
            composable("compras") { ComprasScreen() }
            composable("agregar") { 
                AgregarProductoScreen(
                    onCancel = { nav.popBackStack() },
                    onSaved = { nav.navigate("productos") { popUpTo("productos") { inclusive = true } } }
                )
            }
            composable("agregar/{id}") { entry ->
                val id = entry.arguments?.getString("id")?.toIntOrNull()
                AgregarProductoScreen(
                    onCancel = { nav.popBackStack() },
                    onSaved = {
                        if (id != null) {
                            nav.navigate("detalle/$id") { popUpTo("detalle/$id") { inclusive = true } }
                        } else {
                            nav.navigate("productos") { popUpTo("productos") { inclusive = true } }
                        }
                    },
                    productId = id
                )
            }
            composable("quienes") { QuienesSomosScreen() }
            composable("detalle/{id}") { entry ->
                val id = entry.arguments?.getString("id")?.toIntOrNull() ?: -1
                ProductDetailScreen(
                    productId = id,
                    onBuyNow = { nav.navigate("compras") },
                    onAddToCart = { nav.navigate("carrito") },
                    onEdit = { pid -> nav.navigate("agregar/$pid") }
                )
            }
        }
    }
}

private fun isDestOn(nav: androidx.navigation.NavHostController, route: String): Boolean {
    val entry = nav.currentBackStackEntry
    return entry?.destination?.route == route
}