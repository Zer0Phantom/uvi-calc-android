package de.baumanngeorg.uvilsfrechner.datasource.dwd.model

data class DwdUviForecast(
        val today: Int = 8,
        val tomorrow: Int = 8,
        val dayafter_to: Int = 8
)