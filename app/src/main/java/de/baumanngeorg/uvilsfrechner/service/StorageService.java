package de.baumanngeorg.uvilsfrechner.service;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;

import de.baumanngeorg.uvilsfrechner.datasource.dwd.model.DwdContainer;

public class StorageService {

    private static final String DSGVO = "dsgvo";
    private static final String STADT = "stadt";
    private static final String SKIN_TYPE = "hauttyp";
    private static final String MED_STEPS = "med_schritte";
    private static final String DEFAULT_MED = "default_med";

    private static StorageService instance;

    private final SharedPreferences preferences;

    private StorageService(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void initializeSettingsmanager(Context context) {
        if (instance == null) {
            instance = new StorageService(context);
        }
    }

    public static StorageService getInstance() {
        return instance;
    }

    public String getPreferredCity() {
        return preferences.getString(STADT, "Berlin");
    }

    public int getPreferredSkinType() {
        return Integer.parseInt(preferences.getString(SKIN_TYPE, "2"));
    }

    public int getPreferredMedSteps() {
        return Integer.parseInt(preferences.getString(MED_STEPS, "25"));
    }

    public void setStoredUviContainer(DwdContainer container) {
        preferences.edit().putString(DwdContainer.class.getCanonicalName(), (new Gson()).toJson(container)).apply();
    }

    public DwdContainer getStoredUviContainer() {
        return (new Gson()).fromJson(preferences.getString(DwdContainer.class.getCanonicalName(), null), DwdContainer.class);
    }

    public int getDefaultMed() {
        return Integer.parseInt(preferences.getString(DEFAULT_MED, "2"));
    }

    public void setDefaultMed(int defaultMed) {
        preferences.edit().putString(DEFAULT_MED, String.valueOf(defaultMed)).apply();
    }

    public boolean isDsgvoAccepted() {
        return "true".equals(preferences.getString(DSGVO, "false"));
    }

    public void setDsgvoAccepted() {
        preferences.edit().putString(DSGVO, "true").apply();
    }
}
