package de.baumanngeorg.uvilsfrechner.datasources.dwd

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import de.baumanngeorg.uvilsfrechner.config.InternetResourceLoader
import de.baumanngeorg.uvilsfrechner.datasources.dwd.model.DwdResponse
import de.baumanngeorg.uvilsfrechner.config.StorageService
import de.baumanngeorg.uvilsfrechner.view.main.CalculationFragment
import java.nio.charset.StandardCharsets
import java.util.*

object DwdClient {
    private val gson: Gson = Gson()
    private const val url: String = "https://opendata.dwd.de/climate_environment/health/alerts/uvi.json"
    private var dwdModel: DwdResponse = StorageService.dwdModel

    fun setUvi(calculationFragment: CalculationFragment) {
        val today = Calendar.getInstance()
        val cityName = StorageService.preferredCity

        if (today.after(dwdModel.getNextUpdateCalendar())) {
            retrieveNewData(calculationFragment, cityName)
        } else {
            calculationFragment.setUviSeekbar(dwdModel.getTodaysForecast(cityName))
            calculationFragment.setUpdateString(dwdModel.getUpdateString(cityName))
        }
    }

    private fun retrieveNewData(calculationFragment: CalculationFragment, cityName: String) {
        val stringRequest = StringRequest(
            Request.Method.GET, url, Response.Listener {
            response: String ->
            val ptext = response.toByteArray(StandardCharsets.ISO_8859_1)
            val value = String(ptext, StandardCharsets.UTF_8)
            dwdModel = gson.fromJson(value, DwdResponse::class.java)
            StorageService.dwdModel = dwdModel
            calculationFragment.setUviSeekbar(dwdModel.getTodaysForecast(cityName))
            calculationFragment.setUpdateString(dwdModel.getUpdateString(cityName))
        }, Response.ErrorListener {})

        stringRequest.tag = "UVI"
        InternetResourceLoader.addRequest(stringRequest)
    }
}
