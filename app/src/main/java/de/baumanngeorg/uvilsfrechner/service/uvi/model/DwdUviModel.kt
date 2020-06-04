package de.baumanngeorg.uvilsfrechner.service.uvi.model

data class DwdUviModel(
        val last_update: String = "2000-01-01T10:10:10",
        val next_update: String = "2000-01-01T10:10:10",
        val forecast_day: String = "XTX",
        val name: String = "XTX",
        val sender: String = "XTX",
        val content: List<DwdUviContent> = listOf(
                DwdUviContent(
                        city = "Berlin",
                        forecast = DwdUviForecast(
                                today = 8,
                                tomorrow = 8,
                                dayafter_to = 8
                        )
                )
        )
)