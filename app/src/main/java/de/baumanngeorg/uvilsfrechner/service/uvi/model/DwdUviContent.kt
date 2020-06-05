package de.baumanngeorg.uvilsfrechner.service.uvi.model

data class DwdUviContent(
        val city: String = "Berlin",
        val forecast: DwdUviForecast = DwdUviForecast()
)