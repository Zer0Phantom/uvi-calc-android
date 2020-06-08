package de.baumanngeorg.uvilsfrechner.datasource.dwd.model

data class DwdUviContent(
        val city: String = "Berlin",
        val forecast: DwdUviForecast = DwdUviForecast()
)