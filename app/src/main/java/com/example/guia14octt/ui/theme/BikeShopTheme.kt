package com.example.guia14octt.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Blue = Color(0xFF3F7AFE)
private val Indigo = Color(0xFF4C5BD4)
private val Mint = Color(0xFF36D1A8)
private val SurfaceLight = Color(0xFFF7F7FA)
private val SurfaceDark = Color(0xFF121316)
private val TextPrimaryLight = Color(0xFF111213)
private val TextSecondaryLight = Color(0xFF5E6572)

private val LightColors: ColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    secondary = Mint,
    onSecondary = Color.Black,
    tertiary = Indigo,
    background = Color.White,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFE9EBF1),
    outline = Color(0xFFCDD2DD),
)

private val DarkColors: ColorScheme = darkColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    secondary = Mint,
    onSecondary = Color.Black,
    tertiary = Indigo,
    background = SurfaceDark,
    onBackground = Color(0xFFE6E8EC),
    surface = Color(0xFF1A1B1E),
    onSurface = Color(0xFFE6E8EC),
    surfaceVariant = Color(0xFF2A2C31),
    outline = Color(0xFF454B57),
)

private val AppShapes = Shapes(
    // Rounded, más "pill" y tarjetas suaves
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
)

@Composable
fun BikeShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = androidx.compose.material3.Typography(),
        shapes = AppShapes,
        content = content
    )
}