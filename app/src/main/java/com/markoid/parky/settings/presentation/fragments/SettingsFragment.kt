package com.markoid.parky.settings.presentation.fragments

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.markoid.parky.R
import com.markoid.parky.home.presentation.callbacks.HomeNavigationCallbacks

class SettingsFragment : PreferenceFragmentCompat() {

    private var navigationListener: HomeNavigationCallbacks? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey)
        navigationListener?.onUpdateToolbarMenuItems()
        findPreference<SwitchPreference>(getString(R.string.dark_mode_key))?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                val theme = if (newValue as Boolean) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(theme)
                true
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeNavigationCallbacks) navigationListener = context
    }
}
