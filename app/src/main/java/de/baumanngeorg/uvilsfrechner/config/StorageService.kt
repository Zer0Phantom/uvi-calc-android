package de.baumanngeorg.uvilsfrechner.config

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import de.baumanngeorg.uvilsfrechner.datasources.dwd.model.DwdResponse

object StorageService {

    private const val DSGVO = "dsgvo"
    private const val STADT = "stadt"
    private const val SKIN_TYPE = "hauttyp"
    private const val MED_STEPS = "med_schritte"
    private const val DEFAULT_MED = "default_med"
    private const val DWD_MODEL = "dwd-model"

    private val gsonInstance = Gson()
    private var preferences: SharedPreferences? = null

    fun initialiseService(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    val preferredCity: String
        get() = preferences?.getString(STADT, "Berlin")!!

    val preferredSkinType: Int
        get() = preferences?.getString(SKIN_TYPE, "2")!!.toInt()

    val preferredMedSteps: Int
        get() = preferences?.getString(MED_STEPS, "25")!!.toInt()

    var dwdModel: DwdResponse
        get() {
            val modelString = gsonInstance.toJson(DwdResponse())
            return gsonInstance.fromJson(preferences?.getString(DWD_MODEL, modelString), DwdResponse::class.java)
        }
        set(model) {
            preferences?.edit()?.putString(DWD_MODEL, gsonInstance.toJson(model))?.apply()
        }

    var defaultMed: Int
        get() = preferences?.getString(DEFAULT_MED, "2")!!.toInt()
        set(defaultMed) {
            preferences?.edit()?.putString(DEFAULT_MED, defaultMed.toString())?.apply()
        }

    val isDsgvoAccepted: Boolean
        get() = "true" == preferences?.getString(DSGVO, "false")

    fun setDsgvoAccepted() {
        preferences?.edit()?.putString(DSGVO, "true")?.apply()
    }
}