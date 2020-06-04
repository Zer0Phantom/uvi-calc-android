package de.baumanngeorg.uvilsfrechner.service.uvi.model

data class DwdUviContent(
        val city: String = "",
        val forecast: DwdUviForecast = DwdUviForecast()
)