package de.baumanngeorg.uvilsfrechner.service.uvi.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DwdContainer {
    var model = baseModel

    constructor(model: DwdUviModel) {
        this.model = model
    }

    constructor() {}

    private val baseModel: DwdUviModel
        private get() = DwdUviModel.builder()
                .forecast_day("XTX")
                .last_update("2000-01-01T10:10:10")
                .next_update("2000-01-01T10:10:10")
                .sender("XTX")
                .name("XTX")
                .content(listOf(DwdUviContent("Berlin", DwdUviForecast(8, 8, 8))))
                .build()

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

    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is DwdContainer) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$model`: Any = model
        val `other$model`: Any = other.model
        return if (`this$model` == null) `other$model` == null else `this$model` == `other$model`
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is DwdContainer
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$model`: Any = model
        result = result * PRIME + (`$model`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "DwdContainer(model=" + model + ")"
    }
}