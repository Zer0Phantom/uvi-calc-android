package de.baumanngeorg.uvilsfrechner.datasources.dwd.model

data class DwdUviContent(
    val city: String = "Berlin",
    val forecast: DwdUviForecast = DwdUviForecast()
)