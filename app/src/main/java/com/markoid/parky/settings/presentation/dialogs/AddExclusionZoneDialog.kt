package com.markoid.parky.settings.presentation.dialogs

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.extensions.latLng
import com.markoid.parky.core.presentation.extensions.subscribe
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel
import com.markoid.parky.settings.domain.requests.ExclusionZoneRequest
import com.markoid.parky.settings.presentation.callbacks.ExclusionZoneDialogCallback
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class AddExclusionZoneDialog : CrudExclusionZoneDialog() {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    private val homeViewModel by viewModels<HomeViewModel>()

    private var mListener: ExclusionZoneDialogCallback? = null

    override val isDarkMode: Boolean
        get() = devicePreferences.isDarkModeEnabled

    override fun getStyle(): Int = R.style.FullWidthDialog

    override fun onGetRealTimeLocationUpdates() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.getRealTimeLocation().collect {
                updateUserLocation(it.latLng)
                centerCameraOnLocation(it.latLng)
            }
        }
    }

    override fun onUpdateExclusionZone(zoneToUpdate: ExclusionZoneRequest) {
        homeViewModel.updateExclusionZone(zoneToUpdate).getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> handleResults(it.data)
                is DataState.Error -> showError(it.error)
            }
        }
    }

    override fun onSaveNewExclusionZone(zone: ExclusionZoneRequest) {
        homeViewModel.saveExclusionZone(zone).getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> handleResults(it.data)
                is DataState.Error -> showError(it.error)
            }
        }
    }

    override fun onRefreshAdapter() {
        this.mListener?.onRefreshAdapter()
    }

    fun setListener(listener: ExclusionZoneDialogCallback): AddExclusionZoneDialog {
        this.mListener = listener
        return this
    }

    companion object {
        const val EXCLUSION_ZONE_ARG = "EXCLUSION.ZONE.ARG"
        fun newEditingInstance(
            exclusionZone: ExclusionZoneEntity
        ): AddExclusionZoneDialog = AddExclusionZoneDialog().apply {
            arguments = Bundle().apply { putSerializable(EXCLUSION_ZONE_ARG, exclusionZone) }
        }
    }
}
