package de.baumanngeorg.uvilsfrechner.datasources.dwd.model

data class DwdUviModel(
        val last_update: String = "2000-01-01T10:10:10",
        val next_update: String = "2000-01-01T10:10:10",
        val forecast_day: String = "2000-01-01",
        val name: String = "XTX",
        val sender: String = "XTX",
        val content: List<DwdUviContent> = listOf(DwdUviContent())
)