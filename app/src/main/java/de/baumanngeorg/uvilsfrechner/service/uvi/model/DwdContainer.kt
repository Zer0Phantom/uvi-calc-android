package de.baumanngeorg.uvilsfrechner.service.uvi.model

import java.text.SimpleDateFormat
import java.util.*

data class DwdContainer(
        var model: DwdUviModel = DwdUviModel()
) {

    fun getUpdateString(city: String): String {
        val forecast = getContentByCity(city).forecast
        return StringBuilder("Vorhersage für $city:\n")
                .append("${todayPlus(0)} UV ${forecast.today} | ")
                .append("${todayPlus(1)} UV ${forecast.tomorrow} | ")
                .append("${todayPlus(2)} UV ${forecast.dayafter_to}\n")
                .append("Daten vom ${getLastUpdate?.date}.${getLastUpdate?.month}\n")
                .append("Quelle: ${model.sender}")
                .toString()
    }

    fun getContentByCity(stadt: String): DwdUviContent {
        return model.content
                .filter { city -> stadt == city.city }[0]
    }

    val nextUpdate: Date? = getDateFromDwdString(model.next_update)

    private fun todayPlus(amountDays: Int) : String {
        val date = forecastDayDate()
        date.add(Calendar.DAY_OF_MONTH, amountDays)
        return date.time.toString("dd.MM.")
    }

    private fun forecastDayDate() : Calendar {
        val date = model.forecast_day.split("-").map { it.toInt() }
        val calendar = Calendar.getInstance()
        calendar.set(date[0], date[1]-1, date[2])
        return calendar
    }

    private val getLastUpdate: Date? = getDateFromDwdString(model.last_update)

    private fun getDateFromDwdString(dateString: String) = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMANY).parse(dateString)

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}