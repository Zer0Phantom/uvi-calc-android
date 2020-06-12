package de.baumanngeorg.uvilsfrechner.datasources.dwd.model

data class UviForecast(
    val today: Int = 8,
    val tomorrow: Int = 8,
    val dayafter_to: Int = 8
)