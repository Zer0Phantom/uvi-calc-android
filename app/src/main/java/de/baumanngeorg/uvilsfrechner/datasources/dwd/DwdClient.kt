package de.baumanngeorg.uvilsfrechner.datasources.dwd

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import de.baumanngeorg.uvilsfrechner.config.InternetResourceLoader
import de.baumanngeorg.uvilsfrechner.datasources.dwd.model.DwdContainer
import de.baumanngeorg.uvilsfrechner.datasources.dwd.model.DwdUviContent
import de.baumanngeorg.uvilsfrechner.datasources.dwd.model.DwdUviModel
import de.baumanngeorg.uvilsfrechner.service.StorageService
import de.baumanngeorg.uvilsfrechner.view.main.CalculationFragment
import java.nio.charset.StandardCharsets
import java.util.*

object DwdClient {
    private val gson: Gson = Gson()
    private const val url: String = "https://opendata.dwd.de/climate_environment/health/alerts/uvi.json"
    private var container: DwdContainer = StorageService.storedUviContainer

    fun setUvi(calculationFragment: CalculationFragment) {
        val today = Calendar.getInstance()
        val stadt = StorageService.preferredCity
        val uviContent = container.getContentByCity(stadt)
        if (today.after(container.nextUpdate)) {
            val stringRequest = StringRequest(
                    Request.Method.GET, url, Response.Listener { response: String ->
                val ptext = response.toByteArray(StandardCharsets.ISO_8859_1)
                val value = String(ptext, StandardCharsets.UTF_8)
                container.model = gson.fromJson(value, DwdUviModel::class.java)
                val uviContentNew = container.getContentByCity(stadt)
                StorageService.storedUviContainer = container
                calculationFragment.setUviSeekbar(getUviDependendOnDate(today, uviContentNew))
                calculationFragment.setUpdateString(container.getUpdateString(uviContentNew.city))
            }, Response.ErrorListener { error: VolleyError? -> })
            stringRequest.tag = "UVI"
            InternetResourceLoader.addRequest(stringRequest, calculationFragment.context)
        } else {
            calculationFragment.setUviSeekbar(getUviDependendOnDate(today, uviContent))
            calculationFragment.setUpdateString(container.getUpdateString(uviContent.city))
        }
    }

    private fun getUviDependendOnDate(today: Calendar, uviContent: DwdUviContent): Int {
        return when {
            today.before(container.forecastDayPlusDays(1)) -> uviContent.forecast.today
            today.before(container.forecastDayPlusDays(2)) -> uviContent.forecast.tomorrow
            else -> uviContent.forecast.dayafter_to
        }
    }
}