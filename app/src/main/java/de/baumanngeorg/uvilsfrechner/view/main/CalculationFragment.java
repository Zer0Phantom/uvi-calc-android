package de.baumanngeorg.uvilsfrechner.view.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import de.baumanngeorg.uvilsfrechner.R;
import de.baumanngeorg.uvilsfrechner.service.storage.StorageManager;
import de.baumanngeorg.uvilsfrechner.service.uvi.SunRiseSetCalc;
import de.baumanngeorg.uvilsfrechner.service.uvi.UviCalculationFramework;
import de.baumanngeorg.uvilsfrechner.service.uvi.UviRetrievingService;

public class CalculationFragment extends Fragment {
    private TextView tvUvi;
    private SeekBar sbUvi;

    private TextView tvMed;
    private SeekBar sbMed;

    private TextView tvLsf;
    private TextView tvHowLongOutside;
    private SeekBar sbLsf;

    private TextView tvZeit;
    private TextView tvWhatLsf;
    private SeekBar sbZeit;

    private EditText etTimeSpend;

    private TextView tvUviInfo;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUvi = Objects.requireNonNull(getView()).findViewById(R.id.tvUvi);
        sbUvi = getView().findViewById(R.id.sbUvi);

        tvMed = getView().findViewById(R.id.tvMed);
        sbMed = getView().findViewById(R.id.sbMed);

        tvLsf = getView().findViewById(R.id.tvLsf);
        tvHowLongOutside = getView().findViewById(R.id.tvHowLongOutside);
        sbLsf = getView().findViewById(R.id.sbLsf);

        tvZeit = getView().findViewById(R.id.tvZeit);
        tvWhatLsf = getView().findViewById(R.id.tvWhatLsf);
        sbZeit = getView().findViewById(R.id.sbZeit);

        etTimeSpend = getView().findViewById(R.id.etSpendTime);

        tvUviInfo = getView().findViewById(R.id.tvUviTextInfo);
    }

    @Override
    public void onStart() {
        SeekBar.OnSeekBarChangeListener sbOnChangelistener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSeekBarValues();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        sbUvi.setOnSeekBarChangeListener(sbOnChangelistener);
        sbMed.setOnSeekBarChangeListener(sbOnChangelistener);
        sbLsf.setOnSeekBarChangeListener(sbOnChangelistener);
        sbZeit.setOnSeekBarChangeListener(sbOnChangelistener);

        etTimeSpend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSeekBarValues();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sbMed.setProgress(StorageManager.getInstance().getDefaultMed());

        super.onStart();
    }

    @Override
    public void onResume() {
        UviRetrievingService.getInstance().setUvi(this);
        sbZeit.setProgress((int) Math.round(((SunRiseSetCalc.getSunshineDuration() - 30D) / 30D)), true);
        setMedScale();
        updateSeekBarValues();

        super.onResume();
    }

    public void setUviSeekbar(int value) {
        sbUvi.setProgress(value, true);
    }

    public void setUpdateString(String string) {
        tvUviInfo.setText(string);
    }

    private void updateSeekBarValues() {
        UviCalculationFramework uviCalcFw = UviCalculationFramework.builder()
                .minAlreadySpend(getMinAlreadySpend())
                .uvi(getUvi())
                .med(getMed())
                .lsf(getLsf())
                .zeit(getZeit())
                .build();

        tvHowLongOutside.setText(uviCalcFw.getHowLongOutside());
        tvWhatLsf.setText(uviCalcFw.getWhatLsf());
    }

    private int getUvi() {
        int uvi = sbUvi.getProgress();
        tvUvi.setText(String.valueOf(uvi));
        return uvi;
    }

    private int getLsf() {
        int lsf = sbLsf.getProgress() * 5;
        tvLsf.setText(String.valueOf(lsf));
        return lsf;
    }

    private int getMed() {
        StorageManager.getInstance().setDefaultMed(sbMed.getProgress());
        int[] scaleParams = getMedScale();
        int med = sbMed.getProgress() * scaleParams[2] + scaleParams[1];
        tvMed.setText(String.valueOf(med));
        return med;
    }

    private void setMedScale() {
        int[] scaleParams = getMedScale();
        sbMed.setMax((scaleParams[0] - scaleParams[1]) / scaleParams[2]);
        sbMed.setProgress(StorageManager.getInstance().getDefaultMed());
    }

    /**
     * @return [max, min, steps]
     */
    private int[] getMedScale() {
        int min = 250, max = 400;
        switch (StorageManager.getInstance().getPreferredSkinType()) {
            case 1:
                min = 150;
                max = 300;
                break;
            case 2:
                min = 250;
                max = 400;
                break;
            case 3:
                min = 300;
                max = 500;
                break;
            case 4:
                min = 450;
                max = 600;
                break;
            case 5:
                min = 600;
                max = 900;
                break;
            case 6:
                min = 900;
                max = 1500;
                break;
        }
        int steps = StorageManager.getInstance().getPreferredMedSteps();
        return new int[]{max, min, steps};
    }

    private int getMinAlreadySpend() {
        String[] timeAlreadySpend = etTimeSpend.getText().toString().split(":");
        if (timeAlreadySpend[0].equals("")) {
            timeAlreadySpend[0] = "0";
        }
        int minAlreadySpend;
        switch (timeAlreadySpend.length) {
            case 1:
                minAlreadySpend = Integer.parseInt(timeAlreadySpend[0]);
                break;
            case 2:
            default:
                minAlreadySpend = Integer.parseInt(timeAlreadySpend[0]) * 60 + Integer.parseInt(timeAlreadySpend[1]);
                break;
        }
        return minAlreadySpend;
    }

    private int getZeit() {
        int zeitInMiunten = sbZeit.getProgress() * 30 + 30;
        String zeitText = zeitInMiunten / 60 + ":" + (zeitInMiunten % 60 == 0 ? "00" : "30");
        tvZeit.setText(zeitText);
        return zeitInMiunten;
    }
}
