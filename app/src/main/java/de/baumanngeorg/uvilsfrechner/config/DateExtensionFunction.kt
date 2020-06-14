package de.baumanngeorg.uvilsfrechner.config

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun Calendar.toString(format: String, locale: Locale = Locale.getDefault()): String {
    return this.time.toString(format, locale)
}

/**
 * returns the Calendar's date as dd.MM. -> 2020-09-31 -> 31.09.
 */
fun Calendar.toDdMmString(): String {
    return this.toString("dd.MM.")
}

/**
 * returns the Calendar's date as dd.MM. HH:mm -> 2020-09-31_15-30 -> 31.09. 15:30 Uhr
 */
fun Calendar.toDdMmHhMmString(): String {
    return this.toString("dd.MM.' 'HH:mm' Uhr'")
}