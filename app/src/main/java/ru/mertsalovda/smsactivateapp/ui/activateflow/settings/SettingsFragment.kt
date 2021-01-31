package ru.mertsalovda.smsactivateapp.ui.activateflow.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ru.mertsalovda.smsactivateapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)
    }
}