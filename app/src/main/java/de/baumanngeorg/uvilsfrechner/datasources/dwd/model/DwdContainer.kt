package de.baumanngeorg.uvilsfrechner.datasources.dwd.model

import de.baumanngeorg.uvilsfrechner.config.toString
import java.util.*

data class DwdContainer(
        var model: DwdUviModel = DwdUviModel()
) {

    fun getUpdateString(city: String): String {
        val forecast = getContentByCity(city).forecast
        return StringBuilder("Vorhersage für $city:\n")
                .append("${forecastDayPlusDays(0)} UV ${forecast.today} | ")
                .append("${forecastDayPlusDays(1)} UV ${forecast.tomorrow} | ")
                .append("${forecastDayPlusDays(2)} UV ${forecast.dayafter_to}\n")
                .append("Daten vom ${lastUpdate.time.toString("dd.MM.' 'hh:mm' Uhr'")}\n")
                .append("Quelle: ${model.sender}")
                .toString()
    }

    fun getContentByCity(stadt: String): DwdUviContent {
        return model.content
                .filter { city -> stadt == city.city }[0]
    }

    val nextUpdate = getDateFromDwdString(model.next_update)

    fun forecastDayPlusDays(amountDays: Int): String {
        val date = forecastDay()
        date.add(Calendar.DAY_OF_MONTH, amountDays)
        return date.time.toString("dd.MM.")
    }

    private fun forecastDay(): Calendar {
        val date = model.forecast_day.split("-").map { it.toInt() }
        val calendar = Calendar.getInstance()
        calendar.set(date[0], date[1] - 1, date[2])
        return calendar
    }

    private val lastUpdate = getDateFromDwdString(model.last_update)

    private fun getDateFromDwdString(dateString: String): Calendar {
        val date = dateString.split("T")[0].split("-").map { it.toInt() }
        val time = dateString.split("T")[1].split(":").map { it.toInt() }
        val calendar = Calendar.getInstance()
        calendar.set(date[0], date[1] - 1, date[2], time[0], time[1], time[2])
        return calendar
    }
}