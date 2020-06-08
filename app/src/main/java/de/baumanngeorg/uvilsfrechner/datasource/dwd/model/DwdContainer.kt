package de.baumanngeorg.uvilsfrechner.datasource.dwd.model

import de.baumanngeorg.uvilsfrechner.config.toString
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
                .append("Daten vom ${getLastUpdate()}\n")
                .append("Quelle: ${model.sender}")
                .toString()
    }

    fun getContentByCity(stadt: String): DwdUviContent {
        return model.content
                .filter { city -> stadt == city.city }[0]
    }

    val nextUpdate: Date? = getDateFromDwdString(model.next_update)

    private fun todayPlus(amountDays: Int): String {
        val date = forecastDayDate()
        date.add(Calendar.DAY_OF_MONTH, amountDays)
        return date.time.toString("dd.MM.")
    }

    private fun forecastDayDate(): Calendar {
        val date = model.forecast_day.split("-").map { it.toInt() }
        val calendar = Calendar.getInstance()
        calendar.set(date[0], date[1] - 1, date[2])
        return calendar
    }

    private fun getLastUpdate(): String {
        return getDateFromDwdString(model.last_update).toString("dd.MM.' 'hh:mm' Uhr'")
    }

    private fun getDateFromDwdString(dateString: String): Date {
        val date = dateString.split("T")[0].split("-")
        val time = dateString.split("T")[1].split(":")
        val calendar = Calendar.getInstance()
        calendar.set(date[0].toInt(), date[1].toInt() - 1, date[2].toInt(), time[0].toInt(), time[1].toInt(), time[2].toInt())
        return calendar.time
    }
}