package de.baumanngeorg.uvilsfrechner.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.baumanngeorg.uvilsfrechner.R;
import de.baumanngeorg.uvilsfrechner.datasource.InternetResourceLoader;
import de.baumanngeorg.uvilsfrechner.service.StorageService;
import de.baumanngeorg.uvilsfrechner.view.about.AboutActivity;
import de.baumanngeorg.uvilsfrechner.view.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load Preferences
        StorageService.initializeSettingsmanager(this.getApplicationContext());

        // Load Network Connection
        InternetResourceLoader.initializeInternetResourceLoader(this.getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (StorageService.getInstance().isDsgvoAccepted()) {
            onCreateAfterDsgvo();
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                    .setMessage("Der DWD speichert die IP für maximal sieben Tage zur Verbesserung des Service.")
                    .setTitle("Datenschutzbedingungen")
                    .setPositiveButton("Zustimmen", (dialog, which) -> {
                        StorageService.getInstance().setDsgvoAccepted();
                        onCreateAfterDsgvo();
                    })
                    .setNegativeButton("Ablehnen", (dialog, which) -> MainActivity.super.finish())
                    .setOnDismissListener(dialog -> {
                        if (StorageService.getInstance().isDsgvoAccepted()) {
                            onCreateAfterDsgvo();
                        } else {
                            MainActivity.super.finish();
                        }
                    });

            AlertDialog dialog = alertBuilder.create();
            dialog.show();
        }
    }

    private void onCreateAfterDsgvo() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, new CalculationFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
