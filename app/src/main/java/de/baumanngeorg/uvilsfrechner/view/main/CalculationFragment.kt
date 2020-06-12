package de.baumanngeorg.uvilsfrechner.view.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.baumanngeorg.uvilsfrechner.R
import de.baumanngeorg.uvilsfrechner.config.StorageService.defaultMed
import de.baumanngeorg.uvilsfrechner.config.StorageService.preferredMedSteps
import de.baumanngeorg.uvilsfrechner.config.StorageService.preferredSkinType
import de.baumanngeorg.uvilsfrechner.datasources.dwd.DwdClient.setUvi
import de.baumanngeorg.uvilsfrechner.service.SunRiseSetCalculationService.getSunshineDuration
import de.baumanngeorg.uvilsfrechner.service.UviCalculationHub
import kotlin.math.roundToInt

class CalculationFragment : Fragment() {

    private var tvUvi: TextView? = null
    private var sbUvi: SeekBar? = null
    private var tvMed: TextView? = null
    private var sbMed: SeekBar? = null
    private var tvLsf: TextView? = null
    private var tvHowLongOutside: TextView? = null
    private var sbLsf: SeekBar? = null
    private var tvZeit: TextView? = null
    private var tvWhatLsf: TextView? = null
    private var sbZeit: SeekBar? = null
    private var etTimeSpend: EditText? = null
    private var tvUviInfo: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvUvi = requireView().findViewById(R.id.tvUvi)
        sbUvi = requireView().findViewById(R.id.sbUvi)
        tvMed = requireView().findViewById(R.id.tvMed)
        sbMed = requireView().findViewById(R.id.sbMed)
        tvLsf = requireView().findViewById(R.id.tvLsf)
        tvHowLongOutside = requireView().findViewById(R.id.tvHowLongOutside)
        sbLsf = requireView().findViewById(R.id.sbLsf)
        tvZeit = requireView().findViewById(R.id.tvZeit)
        tvWhatLsf = requireView().findViewById(R.id.tvWhatLsf)
        sbZeit = requireView().findViewById(R.id.sbZeit)
        etTimeSpend = requireView().findViewById(R.id.etSpendTime)
        tvUviInfo = requireView().findViewById(R.id.tvUviTextInfo)
    }

    override fun onStart() {
        val sbOnChangelistener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                updateSeekBarValues()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // No interactions needed
                // here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // no interactions
                // needed here
            }
        }
        sbUvi!!.setOnSeekBarChangeListener(sbOnChangelistener)
        sbMed!!.setOnSeekBarChangeListener(sbOnChangelistener)
        sbLsf!!.setOnSeekBarChangeListener(sbOnChangelistener)
        sbZeit!!.setOnSeekBarChangeListener(sbOnChangelistener)
        etTimeSpend!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // no interactions
                // needed here
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateSeekBarValues()
            }

            override fun afterTextChanged(s: Editable) {
                // no interactions
                // needed here
            }
        })
        sbMed!!.progress = defaultMed
        super.onStart()
    }

    override fun onResume() {
        setUvi(this)
        sbZeit!!.progress = ((getSunshineDuration() - 30.0) / 30.0).roundToInt()
        setMedScale()
        updateSeekBarValues()
        super.onResume()
    }

    fun setUviSeekbar(value: Int) {
        sbUvi!!.progress = value
    }

    fun setUpdateString(string: String?) {
        tvUviInfo!!.text = string
    }

    private fun updateSeekBarValues() {
        val hub = UviCalculationHub(
            minAlreadySpend = minAlreadySpend,
            uvi = uvi,
            med = med,
            lsf = lsf,
            zeit = zeit
        )
        tvHowLongOutside!!.text = hub.howLongOutside
        tvWhatLsf!!.text = hub.whatLsf
    }

    private val uvi: Int
        get() {
            val uvi = sbUvi!!.progress
            tvUvi!!.text = uvi.toString()
            return uvi
        }

    private val lsf: Int
        get() {
            val lsf = sbLsf!!.progress * 5
            tvLsf!!.text = lsf.toString()
            return lsf
        }

    private val med: Int
        get() {
            defaultMed = sbMed!!.progress
            val scaleParams = medScale
            val med = sbMed!!.progress * scaleParams[2] + scaleParams[1]
            tvMed!!.text = med.toString()
            return med
        }

    private fun setMedScale() {
        val scaleParams = medScale
        sbMed!!.max = (scaleParams[0] - scaleParams[1]) / scaleParams[2]
        sbMed!!.progress = defaultMed
    }

    /**
     * @return [max, min, steps]
     */
    private val medScale: IntArray
        get() {
            val min: Int
            val max: Int
            when (preferredSkinType) {
                1 -> {
                    min = 150
                    max = 300
                }

                3 -> {
                    min = 300
                    max = 500
                }

                4 -> {
                    min = 450
                    max = 600
                }

                5 -> {
                    min = 600
                    max = 900
                }

                6 -> {
                    min = 900
                    max = 1500
                }

                else -> {
                    min = 250
                    max = 400
                }
            }
            val steps = preferredMedSteps
            return intArrayOf(max, min, steps)
        }

    private val minAlreadySpend: Int
        get() {
            val timeAlreadySpend = etTimeSpend!!.text.toString().split(":".toRegex()).toTypedArray()
            if (timeAlreadySpend[0] == "") {
                timeAlreadySpend[0] = "0"
            }
            val minAlreadySpend: Int
            minAlreadySpend = when (timeAlreadySpend.size) {
                1 -> timeAlreadySpend[0].toInt()
                2 -> timeAlreadySpend[0].toInt() * 60 + timeAlreadySpend[1].toInt()
                else -> timeAlreadySpend[0].toInt() * 60 + timeAlreadySpend[1].toInt()
            }
            return minAlreadySpend
        }

    private val zeit: Int
        get() {
            val zeitInMiunten = sbZeit!!.progress * 30 + 30
            val zeitText = (zeitInMiunten / 60).toString() + ":" + if (zeitInMiunten % 60 == 0) "00" else "30"
            tvZeit!!.text = zeitText
            return zeitInMiunten
        }
}