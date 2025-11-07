package com.example.guia14octt.ui

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun formatClp(value: Double): String {
    val locale = Locale.forLanguageTag("es-CL")
    val formatter = NumberFormat.getCurrencyInstance(locale)
    formatter.currency = Currency.getInstance("CLP")
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}