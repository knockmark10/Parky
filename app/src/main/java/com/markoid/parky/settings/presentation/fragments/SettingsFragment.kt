package com.markoid.parky.settings.presentation.fragments

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.* // ktlint-disable no-wildcard-imports
import com.markoid.parky.R
import com.markoid.parky.core.presentation.extensions.asMoney
import com.markoid.parky.core.presentation.extensions.show
import com.markoid.parky.home.presentation.callbacks.HomeNavigationCallbacks
import com.markoid.parky.home.presentation.dialgos.MapTypeDialog
import com.markoid.parky.home.presentation.enums.ParkingType
import com.markoid.parky.home.presentation.services.BluetoothService
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    private var navigationListener: HomeNavigationCallbacks? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preference_settings, rootKey)

        navigationListener?.onUpdateToolbarMenuItems()

        setupMapTypePreference()

        setupDarkModePreference()

        setupAutoParkingPreference()

        setupBluetoothDevicePreference()

        setupFavoriteParkingTypePreference()

        setupHourRatePreference()
    }

    private fun setupMapTypePreference() {
        findPreference<Preference>(getString(R.string.map_type))?.apply {
            setOnPreferenceClickListener {
                MapTypeDialog().show(childFragmentManager)
                true
            }
        }
    }

    private fun setupDarkModePreference() {
        findPreference<SwitchPreference>(getString(R.string.dark_mode_key))?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                val theme = if (newValue as Boolean) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(theme)
                true
            }
        }
    }

    private fun setupAutoParkingPreference() {
        findPreference<SwitchPreference>(getString(R.string.auto_detection_enabled_key))?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                changeAutoDetectionParametersAvailability(newValue as Boolean)
                if (newValue) BluetoothService.startBluetoothService(requireContext())
                else BluetoothService.stopBluetoothService(requireContext())
                true
            }
        }
        changeAutoDetectionParametersAvailability(devicePreferences.isAutoParkingDetectionEnabled)
    }

    private fun changeAutoDetectionParametersAvailability(isEnabled: Boolean) {
        findPreference<ListPreference>(getString(R.string.bluetooth_device_key))
            ?.apply { this.isEnabled = isEnabled }
        findPreference<ListPreference>(getString(R.string.favorite_parking_key))
            ?.apply { this.isEnabled = isEnabled }
        findPreference<EditTextPreference>(getString(R.string.internal_hour_rate_key))
            ?.apply { this.isEnabled = isEnabled }
        findPreference<Preference>(getString(R.string.exclusion_zone_key))
            ?.apply { this.isEnabled = isEnabled }
    }

    private fun setupBluetoothDevicePreference() {
        findPreference<ListPreference>(getString(R.string.bluetooth_device_key))?.apply {
            val bluetoothDevices = getBondedBluetoothDevices()
            entries = bluetoothDevices
            entryValues = bluetoothDevices
            summary = devicePreferences.bluetoothDevice
            setOnPreferenceChangeListener { _, newValue ->
                summary = newValue as String
                true
            }
        }
    }

    private fun setupFavoriteParkingTypePreference() {
        findPreference<ListPreference>(getString(R.string.favorite_parking_key))?.apply {
            val parkingTypeList = ParkingType.getLocalizedValues(resources).toTypedArray()
            entries = parkingTypeList
            entryValues = parkingTypeList
            summary = devicePreferences.favoriteParkingType
            setOnPreferenceChangeListener { _, newValue ->
                summary = newValue as String
                true
            }
        }
    }

    private fun setupHourRatePreference() {
        findPreference<EditTextPreference>(getString(R.string.internal_hour_rate_key))?.apply {
            setOnBindEditTextListener {
                it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            }
            summary = devicePreferences.hourRate.asMoney()
            setOnPreferenceChangeListener { _, newValue ->
                val hourRate = (newValue as String).toDouble()
                devicePreferences.hourRate = hourRate
                summary = hourRate.asMoney()
                true
            }
        }
    }

    private fun getBondedBluetoothDevices(): Array<String> {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter.bondedDevices.map { it.name }.toTypedArray()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeNavigationCallbacks) navigationListener = context
    }
}
