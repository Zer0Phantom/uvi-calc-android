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

import de.baumanngeorg.uvilsfrechner.R;
import de.baumanngeorg.uvilsfrechner.datasources.dwd.DwdClient;
import de.baumanngeorg.uvilsfrechner.service.StorageService;
import de.baumanngeorg.uvilsfrechner.service.SunRiseSetCalculationService;
import de.baumanngeorg.uvilsfrechner.service.UviCalculationService;

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

        tvUvi = requireView().findViewById(R.id.tvUvi);
        sbUvi = requireView().findViewById(R.id.sbUvi);

        tvMed = requireView().findViewById(R.id.tvMed);
        sbMed = requireView().findViewById(R.id.sbMed);

        tvLsf = requireView().findViewById(R.id.tvLsf);
        tvHowLongOutside = requireView().findViewById(R.id.tvHowLongOutside);
        sbLsf = requireView().findViewById(R.id.sbLsf);

        tvZeit = requireView().findViewById(R.id.tvZeit);
        tvWhatLsf = requireView().findViewById(R.id.tvWhatLsf);
        sbZeit = requireView().findViewById(R.id.sbZeit);

        etTimeSpend = requireView().findViewById(R.id.etSpendTime);

        tvUviInfo = requireView().findViewById(R.id.tvUviTextInfo);
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
                // No interactions needed
                // here
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // no interactions
                // needed here
            }
        };

        sbUvi.setOnSeekBarChangeListener(sbOnChangelistener);
        sbMed.setOnSeekBarChangeListener(sbOnChangelistener);
        sbLsf.setOnSeekBarChangeListener(sbOnChangelistener);
        sbZeit.setOnSeekBarChangeListener(sbOnChangelistener);

        etTimeSpend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no interactions
                // needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSeekBarValues();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no interactions
                // needed here
            }
        });
        sbMed.setProgress(StorageService.INSTANCE.getDefaultMed());

        super.onStart();
    }

    @Override
    public void onResume() {
        DwdClient.INSTANCE.setUvi(this);
        sbZeit.setProgress((int) Math.round(((SunRiseSetCalculationService.INSTANCE.getSunshineDuration() - 30D) / 30D)));
        setMedScale();
        updateSeekBarValues();

        super.onResume();
    }

    public void setUviSeekbar(int value) {
        sbUvi.setProgress(value);
    }

    public void setUpdateString(String string) {
        tvUviInfo.setText(string);
    }

    private void updateSeekBarValues() {
        UviCalculationService uviCalcFw = UviCalculationService.builder()
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
        StorageService.INSTANCE.setDefaultMed(sbMed.getProgress());
        int[] scaleParams = getMedScale();
        int med = sbMed.getProgress() * scaleParams[2] + scaleParams[1];
        tvMed.setText(String.valueOf(med));
        return med;
    }

    private void setMedScale() {
        int[] scaleParams = getMedScale();
        sbMed.setMax((scaleParams[0] - scaleParams[1]) / scaleParams[2]);
        sbMed.setProgress(StorageService.INSTANCE.getDefaultMed());
    }

    /**
     * @return [max, min, steps]
     */
    private int[] getMedScale() {
        int min;
        int max;
        switch (StorageService.INSTANCE.getPreferredSkinType()) {
            case 1:
                min = 150;
                max = 300;
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
            case 2:
            default:
                min = 250;
                max = 400;
                break;
        }
        int steps = StorageService.INSTANCE.getPreferredMedSteps();
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
