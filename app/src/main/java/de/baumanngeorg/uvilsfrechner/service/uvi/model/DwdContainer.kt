package de.baumanngeorg.uvilsfrechner.service.uvi.model

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

data class DwdContainer(
        val model: DwdUviModel
) {

    fun getUpdateString(city: String): String {
        val forecast = getContentByCity(city)?.forecast ?: DwdUviForecast()
        return """Vorhersage für $city:
${forecastDay.format(DateTimeFormatter.ofPattern("dd.MM.' UV '"))}${forecast.today}${forecastDay.plusDays(1).format(DateTimeFormatter.ofPattern("'  |  'dd.MM.' UV '"))}${forecast.tomorrow}${forecastDay.plusDays(2).format(DateTimeFormatter.ofPattern("'  |  'dd.MM.' UV '"))}${forecast.dayafter_to}
Daten vom ${getLastUpdate.format(DateTimeFormatter.ofPattern("dd.MM.yy', 'hh:mm' Uhr'"))}
Quelle: ${model.sender}"""
    }

    private fun getContentByCity(stadt: String): DwdUviContent? {
        return model.content
                .filter { city -> stadt == city.city }
                .getOrNull(0)
    }

    val nextUpdate = getDateFromDwdString(model.next_update)

    val getLastUpdate = getDateFromDwdString(model.last_update)

    val forecastDay = SimpleDateFormat("yyyy-MM-dd").parse(model.forecast_day)


    private fun getDateFromDwdString(dateString: String) = SimpleDateFormat("yyyy-MM-ddThh:mm:ss").parse(dateString)

    private fun stringArrayToInt(sarray: Array<String>): IntArray {
        return Arrays.stream(sarray).mapToInt { s: String -> s.toInt() }.toArray()
    }
}