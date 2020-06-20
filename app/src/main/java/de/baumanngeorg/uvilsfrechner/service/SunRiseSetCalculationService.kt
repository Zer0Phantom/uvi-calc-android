package de.baumanngeorg.uvilsfrechner.service

import ca.rmen.sunrisesunset.SunriseSunset
import java.util.Calendar
import kotlin.math.roundToLong

internal object SunRiseSetCalculationService {

    fun getSunshineDuration(): Long {
        val dates = SunriseSunset.getSunriseSunset(Calendar.getInstance(), 52.511869, 13.405181)
        return ((dates[1].timeInMillis - dates[0].timeInMillis) / 1000.0 / 60.0 - 60.0).roundToLong()
    }
}
