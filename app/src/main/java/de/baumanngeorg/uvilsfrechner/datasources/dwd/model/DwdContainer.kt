package de.baumanngeorg.uvilsfrechner.datasources.dwd.model

import de.baumanngeorg.uvilsfrechner.config.toDdMmHhMmString
import de.baumanngeorg.uvilsfrechner.config.toDdMmString
import java.util.*

data class DwdContainer(
    var model: DwdUviModel = DwdUviModel()
) {

    val nextUpdate = getDateFromDwdString(model.next_update)

    private fun getDateFromDwdString(dateString: String): Calendar {
        val date = dateString.split("T")[0].split("-").map { it.toInt() }
        val time = dateString.split("T")[1].split(":").map { it.toInt() }
        val calendar = Calendar.getInstance()
        calendar.set(date[0], date[1] - 1, date[2], time[0], time[1], time[2])
        return calendar
    }
}