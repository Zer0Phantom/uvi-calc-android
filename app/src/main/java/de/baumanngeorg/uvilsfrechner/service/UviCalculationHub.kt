package de.baumanngeorg.uvilsfrechner.service

import kotlin.math.roundToInt

data class UviCalculationHub(
    private val uvi: Int = 0,
    private val zeit: Int = 0,
    private val med: Int = 0,
    private var lsf: Int = 0,
    private val minAlreadySpend: Int = 0
) {

    // Calculation Zeit/((MED/(2*UVI) - minAlreadySpend)
    val whatLsf: String
        get() {
            // Calculation Zeit/((MED/(2*UVI) - minAlreadySpend)
            if (uvi == 0) {
                return "Kein LSF notwendig."
            }
            val lsfCalculated = zeit / (med / (2.0 * uvi) - minAlreadySpend) * 2
            return if (lsfCalculated < 0 || lsfCalculated > 55) {
                "Es gibt leider keinen groß genügenden LSF."
            } else "LSF " + lsfCalculated.roundToInt()
        }

    // Calculation: (MED/(2*UVI) - minALreadySpend)*LSF
    val howLongOutside: String
        get() {
            // Calculation: (MED/(2*UVI) - minALreadySpend)*LSF
            if (lsf == 0) {
                lsf = 2
            }
            if (uvi == 0) {
                return "Keine Beschränkung vorhanden."
            }
            val restOutside: Double = (med / (2.0 * uvi) - minAlreadySpend) * (lsf / 2.0)
            val restHoursOutside = restOutside.toInt() / 60
            val restMinutesOutside = restOutside.toInt() % 60
            return (if (restHoursOutside > 0) restHoursOutside.toString() + "h" else "") + if (restMinutesOutside > 0) " " + restMinutesOutside + "min" else " 0min"
        }
}