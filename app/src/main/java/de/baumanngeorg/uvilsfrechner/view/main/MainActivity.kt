package de.baumanngeorg.uvilsfrechner.view.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import de.baumanngeorg.uvilsfrechner.R
import de.baumanngeorg.uvilsfrechner.service.StorageService
import de.baumanngeorg.uvilsfrechner.view.about.AboutActivity
import de.baumanngeorg.uvilsfrechner.view.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StorageService.initialiseService(this.applicationContext)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (StorageService.isDsgvoAccepted) {
            onCreateAfterDsgvo()
        } else {
            val alertBuilder = AlertDialog.Builder(this)
                    .setMessage("Der DWD speichert die IP für maximal sieben Tage zur Verbesserung des Service.")
                    .setTitle("Datenschutzbedingungen")
                    .setPositiveButton("Zustimmen") { _: DialogInterface?, _: Int ->
                        StorageService.setDsgvoAccepted()
                        onCreateAfterDsgvo()
                    }
                    .setNegativeButton("Ablehnen") { _: DialogInterface?, _: Int -> super@MainActivity.finish() }
                    .setOnDismissListener {
                        if (StorageService.isDsgvoAccepted) {
                            onCreateAfterDsgvo()
                        } else {
                            super@MainActivity.finish()
                        }
                    }
            val dialog = alertBuilder.create()
            dialog.show()
        }
    }

    private fun onCreateAfterDsgvo() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, CalculationFragment())
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}