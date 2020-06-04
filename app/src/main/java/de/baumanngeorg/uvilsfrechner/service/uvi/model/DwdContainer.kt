package de.baumanngeorg.uvilsfrechner.service.uvi.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class DwdContainer(
        val model : DwdUviModel
) {

    fun getUpdateString(city: String): String {
        val forecast = getContentByCity(city).forecast
        return """Vorhersage für $city:
${forecastDay.format(DateTimeFormatter.ofPattern("dd.MM.' UV '"))}${forecast.today}${forecastDay.plusDays(1).format(DateTimeFormatter.ofPattern("'  |  'dd.MM.' UV '"))}${forecast.tomorrow}${forecastDay.plusDays(2).format(DateTimeFormatter.ofPattern("'  |  'dd.MM.' UV '"))}${forecast.dayafter_to}
Daten vom ${lastUpdate.format(DateTimeFormatter.ofPattern("dd.MM.yy', 'hh:mm' Uhr'"))}
Quelle: ${model.sender}"""
    }

    fun getContentByCity(stadt: String): DwdUviContent {
        return model.content.stream()
                .filter { city: DwdUviContent -> stadt == city.city }
                .findAny()
                .orElse(baseModel.content[0])
    }

    val nextUpdate: LocalDateTime
        get() = getDateFromDwdString(model.next_update)

    private val lastUpdate: LocalDateTime
        private get() = getDateFromDwdString(model.last_update)

    val forecastDay: LocalDate
        get() {
            val forecastDay = stringArrayToInt(model.forecast_day.split("-".toRegex()).toTypedArray())
            return LocalDate.of(forecastDay[0], forecastDay[1], forecastDay[2])
        }

    private fun getDateFromDwdString(datesting: String): LocalDateTime {
        val dateTime = datesting.split("T".toRegex()).toTypedArray()
        val date = stringArrayToInt(dateTime[0].split("-".toRegex()).toTypedArray())
        val time = stringArrayToInt(dateTime[1].split(":".toRegex()).toTypedArray())
        return LocalDateTime.of(date[0], date[1], date[2], time[0], time[1], time[2])
    }

    private fun stringArrayToInt(sarray: Array<String>): IntArray {
        return Arrays.stream(sarray).mapToInt { s: String -> s.toInt() }.toArray()
    }
}