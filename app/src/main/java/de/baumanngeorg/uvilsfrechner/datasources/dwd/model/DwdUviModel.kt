package de.baumanngeorg.uvilsfrechner.datasources.dwd.model

import de.baumanngeorg.uvilsfrechner.config.toDdMmHhMmString
import de.baumanngeorg.uvilsfrechner.config.toDdMmString
import java.util.*

@Suppress("kotlin:S117")
data class DwdUviModel(
    val last_update: String = "2000-01-01T10:10:10",
    val next_update: String = "2000-01-01T10:10:10",
    val forecast_day: String = "2000-01-01",
    val name: String = "XTX",
    val sender: String = "XTX",
    val content: List<DwdUviContent> = listOf(DwdUviContent())
) {

    fun getLastUpdateCalendar() = stringToCalendar(last_update)
    fun getNextUpdateCalendar() = stringToCalendar(next_update)
    fun getForecastDayCalendar() = stringToCalendar(forecast_day)
    fun getForecastDayCalendar(days: Int): Calendar {
        val date = getForecastDayCalendar()
        date.add(Calendar.DAY_OF_MONTH, days)
        return date
    }

    fun getForecastForCity(city: String): DwdUviForecast {
        return content.single { it.city == city }.forecast
    }

    fun getUpdateString(city: String): String {
        val forecast = getForecastForCity(city)
        return StringBuilder("Vorhersage für $city:\n")
            .append("${getForecastDayCalendar().toDdMmString()} UV ${forecast.today} | ")
            .append("${getForecastDayCalendar(1).toDdMmString()} UV ${forecast.tomorrow} | ")
            .append("${getForecastDayCalendar(2).toDdMmString()} UV ${forecast.dayafter_to}\n")
            .append("Daten vom ${getLastUpdateCalendar().toDdMmHhMmString()}\n")
            .append("Quelle: ${sender}")
            .toString()
    }

    private fun stringToCalendar(dateString: String): Calendar {
        val calendar = Calendar.getInstance()
        val calendarArray = dateString.split("T")
        val dateArray = calendarArray[0].split("-").map { it.toInt() }
        return when (calendarArray.size) {
            1 -> {
                calendar.set(dateArray[0], dateArray[1] - 1, dateArray[2])
                calendar
            }

            else -> {
                val timeArray = calendarArray[1].split(":").map { it.toInt() }
                calendar.set(dateArray[0], dateArray[1] - 1, dateArray[2], timeArray[0], timeArray[1], timeArray[2])
                calendar
            }
        }
    }
}