package com.markoid.parky.settings.presentation.fragments

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.markoid.parky.R
import com.markoid.parky.core.presentation.extensions.BluetoothExtensions
import com.markoid.parky.core.presentation.extensions.LocaleExtensions
import com.markoid.parky.core.presentation.extensions.asMoney
import com.markoid.parky.core.presentation.extensions.show
import com.markoid.parky.home.presentation.callbacks.HomeNavigationCallbacks
import com.markoid.parky.home.presentation.dialgos.MapTypeDialog
import com.markoid.parky.home.presentation.enums.ParkingType
import com.markoid.parky.home.presentation.enums.ParkingType.Companion.getIndexForName
import com.markoid.parky.home.presentation.services.BluetoothService
import com.markoid.parky.permissions.presentation.controllers.LocationPermissionController
import com.markoid.parky.permissions.presentation.enums.LocationPermissions
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    @Inject
    lateinit var permissionController: LocationPermissionController

    private var navigationListener: HomeNavigationCallbacks? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preference_settings, rootKey)

        setupToolbar()

        setupMapTypePreference()

        setupLanguagePreference()

        setupThemePreference()

        setupDarkModePreference()

        setupParkingSpotCleaningPreference()

        setupAutoDetectionCategory()

        setupAutoParkingPreference()

        setupBluetoothDevicePreference()

        setupFavoriteParkingTypePreference()

        setupHourRatePreference()

        setupExclusionZonePreference()
    }

    private fun setupToolbar() {
        navigationListener?.apply {
            onUpdateToolbarMenuItems()
            onSetBackArrowOnToolbar()
        }
    }

    private fun setupMapTypePreference() {
        findPreference<Preference>(getString(R.string.map_type))?.apply {
            setOnPreferenceClickListener {
                MapTypeDialog().show(childFragmentManager)
                true
            }
        }
    }

    private fun setupLanguagePreference() {
        findPreference<ListPreference>(getString(R.string.language_key))?.apply {
            val currentLanguage = Locale.getDefault().language
            val languageIndex = LocaleExtensions.getLocaleIndex(currentLanguage)
            setValueIndex(languageIndex)
            setOnPreferenceChangeListener { _, newValue ->
                val locale = LocaleExtensions.getLocaleForLanguage(newValue as String)
                Lingver.getInstance().setLocale(requireActivity(), locale)
                requireActivity().recreate()
                true
            }
        }
    }

    private fun setupThemePreference() {
        findPreference<ListPreference>(getString(R.string.current_theme_key))?.apply {
            val typedArrayThemes = resources.obtainTypedArray(R.array.theme_values)
            val themesLength = typedArrayThemes.length()
            val themeNames = arrayOfNulls<String>(themesLength)
            for (i in 0 until themesLength) {
                val themeResId = typedArrayThemes.getResourceId(i, 0)
                themeNames[i] = resources.getResourceEntryName(themeResId)
            }
            typedArrayThemes.recycle()
            entryValues = themeNames
            setOnPreferenceChangeListener { _, _ ->
                requireActivity().recreate()
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

    private fun setupParkingSpotCleaningPreference() {
        findPreference<ListPreference>(getString(R.string.clean_up_parking_spot_key))?.apply {
            summary = String.format(
                getString(R.string.clean_up_spot_summary),
                devicePreferences.cleanUpAfterDays
            )
            val entriesList = resources.getStringArray(R.array.clean_up_values).toList()
            val index = entriesList.indexOf(devicePreferences.cleanUpAfterDays.toString())
            setValueIndex(index)
            setOnPreferenceChangeListener { _, newValue ->
                summary = String.format(
                    getString(R.string.clean_up_spot_summary),
                    (newValue as String).toInt()
                )
                true
            }
        }
    }

    private fun setupAutoParkingPreference() {
        findPreference<SwitchPreference>(getString(R.string.auto_detection_enabled_key))?.apply {
            // Update the preference according to the service's running state
            devicePreferences.isAutoParkingDetectionEnabled =
                BluetoothService.isServiceRunning(requireContext())
            // Set the state of the switch according to the service's status
            isChecked = devicePreferences.isAutoParkingDetectionEnabled
            setOnPreferenceChangeListener { _, newValue ->
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    val isPermissionGranted: Boolean =
                        permissionController.onRequestPermission(LocationPermissions.BackgroundLocation)
                    if (isPermissionGranted) {
                        changeAutoDetectionParametersAvailability(newValue as Boolean)
                        if (newValue) BluetoothService.startBluetoothService(requireContext())
                        else BluetoothService.stopBluetoothService(requireContext())
                    } else {
                        isChecked = false
                    }
                }
                true
            }
        }
    }

    private fun setupAutoDetectionCategory() {
        findPreference<PreferenceCategory>(getString(R.string.preference_auto_detection_key))
            ?.isVisible = BluetoothAdapter.getDefaultAdapter() != null
    }

    private fun changeAutoDetectionParametersAvailability(isEnabled: Boolean) {
        findPreference<ListPreference>(getString(R.string.bluetooth_device_key))
            ?.apply { this.isEnabled = isEnabled }
        findPreference<ListPreference>(getString(R.string.favorite_parking_key))
            ?.apply { this.isEnabled = isEnabled }
        findPreference<EditTextPreference>(getString(R.string.internal_hour_rate_key))
            ?.apply { this.isEnabled = isEnabled }
        findPreference<Preference>(getString(R.string.internal_exclusion_zone_key))
            ?.apply { this.isEnabled = isEnabled }
    }

    private fun setupBluetoothDevicePreference() {
        findPreference<ListPreference>(getString(R.string.bluetooth_device_key))?.apply {
            // Display devices with an option for 'any'
            entries = BluetoothExtensions.getBondedDevicesWithAny(getString(R.string.any))
            // Values to be stored, with any as '-'
            entryValues = BluetoothExtensions.getBondedDevicesWithAny("-")
            summary = devicePreferences.bluetoothDevice
            setOnPreferenceChangeListener { _, newValue ->
                summary = newValue as String
                true
            }
        }
    }

    private fun setupFavoriteParkingTypePreference() {
        findPreference<ListPreference>(getString(R.string.favorite_parking_key))?.apply {
            // Translated values
            entries = ParkingType.getLocalizedValues(resources).toTypedArray()
            // Values to be stored, with the name of the enum to prevent issues while changing language
            entryValues = ParkingType.values().map { it.name }.toTypedArray()
            // Displaying value even when there is none stored
            setValueIndex(getIndexForName(devicePreferences.favoriteParkingType))
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

    private fun setupExclusionZonePreference() {
        findPreference<Preference>(getString(R.string.internal_exclusion_zone_key))?.apply {
            setOnPreferenceClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionToExclusionZones())
                true
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeNavigationCallbacks) navigationListener = context
    }
}
