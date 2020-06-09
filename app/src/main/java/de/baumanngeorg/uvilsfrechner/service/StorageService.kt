package de.baumanngeorg.uvilsfrechner.service

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import de.baumanngeorg.uvilsfrechner.datasources.dwd.model.DwdContainer

object StorageService {

    private const val DSGVO = "dsgvo"
    private const val STADT = "stadt"
    private const val SKIN_TYPE = "hauttyp"
    private const val MED_STEPS = "med_schritte"
    private const val DEFAULT_MED = "default_med"

    private var context: Context? = null
    private var preferences: SharedPreferences? = null

    fun initialiseService(context: Context) {
        this.context = context
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    val preferredCity: String
        get() = preferences?.getString(STADT, "Berlin")!!

    val preferredSkinType: Int
        get() = preferences?.getString(SKIN_TYPE, "2")!!.toInt()

    val preferredMedSteps: Int
        get() = preferences?.getString(MED_STEPS, "25")!!.toInt()

    var storedUviContainer: DwdContainer
        get() {
            val containerString = Gson().toJson(DwdContainer())
            return Gson().fromJson(preferences?.getString(DwdContainer::class.java.canonicalName, containerString), DwdContainer::class.java)
        }
        set(container) {
            preferences?.edit()?.putString(DwdContainer::class.java.canonicalName, Gson().toJson(container))?.apply()
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