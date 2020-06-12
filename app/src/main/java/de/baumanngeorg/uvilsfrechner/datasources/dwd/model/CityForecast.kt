package de.baumanngeorg.uvilsfrechner.datasources.dwd.model

data class CityForecast(
    val city: String = "Berlin",
    val forecast: UviForecast = UviForecast()
)