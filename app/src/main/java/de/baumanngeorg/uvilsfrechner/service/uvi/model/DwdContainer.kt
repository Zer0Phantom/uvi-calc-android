package de.baumanngeorg.uvilsfrechner.service.uvi.model

import java.text.SimpleDateFormat
import java.util.*

data class DwdContainer(
        var model: DwdUviModel = DwdUviModel()
) {

    fun getUpdateString(city: String): String {
        return ""
    }

    fun getContentByCity(stadt: String): DwdUviContent? {
        return model.content
                .filter { city -> stadt == city.city }
                .getOrNull(0)
    }

    val nextUpdate: Date? = getDateFromDwdString(model.next_update)

    val forecastDay: Date? = SimpleDateFormat("yyyy-MM-dd").parse(model.forecast_day)

    private val getLastUpdate: Date? = getDateFromDwdString(model.last_update)

    private fun getDateFromDwdString(dateString: String) = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateString)
}