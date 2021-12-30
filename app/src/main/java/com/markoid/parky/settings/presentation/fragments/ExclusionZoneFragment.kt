package com.markoid.parky.settings.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.appAlert
import com.markoid.parky.core.presentation.extensions.findMapById
import com.markoid.parky.core.presentation.extensions.show
import com.markoid.parky.core.presentation.extensions.subscribe
import com.markoid.parky.core.presentation.extensions.verticalLayoutManager
import com.markoid.parky.databinding.FragmentExclusionZoneBinding
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.home.presentation.fragments.HomeBaseFragment
import com.markoid.parky.position.presentation.extensions.centerWithLatLngList
import com.markoid.parky.position.presentation.extensions.drawCircle
import com.markoid.parky.position.presentation.extensions.setDarkMode
import com.markoid.parky.settings.presentation.adapters.ExclusionZonesAdapter
import com.markoid.parky.settings.presentation.callbacks.ExclusionZoneDialogCallback
import com.markoid.parky.settings.presentation.callbacks.ExclusionZonesAdapterCallback
import com.markoid.parky.settings.presentation.dialogs.AddExclusionZoneDialog
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExclusionZoneFragment :
    HomeBaseFragment<FragmentExclusionZoneBinding>(),
    ExclusionZoneDialogCallback,
    ExclusionZonesAdapterCallback {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    private lateinit var mGoogleMap: GoogleMap

    private val exclusionZonesAdapter by lazy { ExclusionZonesAdapter() }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExclusionZoneBinding = FragmentExclusionZoneBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        setupMap()
    }

    private fun setupMap() =
        childFragmentManager.findMapById(R.id.exclusion_zones_map)?.getMapAsync { handleMap(it) }

    private fun handleMap(map: GoogleMap) {
        this.mGoogleMap = map
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (devicePreferences.isDarkModeEnabled) map.setDarkMode(requireContext())
        setupView()
    }

    private fun setupView() {
        setupExclusionZonesList()
        setClickListener()
    }

    private fun setupToolbar() {
        navigationListener?.onSetBackArrowOnToolbar()
    }

    private fun setupExclusionZonesList() {
        onRefreshAdapter()
        exclusionZonesAdapter.setOnAdapterClickListener(this)
        binding.exclusionZonesList.layoutManager = requireContext().verticalLayoutManager
        binding.exclusionZonesList.adapter = exclusionZonesAdapter
    }

    private fun setClickListener() {
        binding.actionAddExclusionZone.setOnClickListener {
            AddExclusionZoneDialog().setListener(this).show(childFragmentManager)
        }
    }

    private fun refreshView(zones: List<ExclusionZoneEntity>) {
        binding.exclusionZoneEmptyStateMessage.isVisible = zones.isEmpty()
        exclusionZonesAdapter.setExclusionZones(zones)
        mGoogleMap.clear()
        if (zones.isNotEmpty()) {
            zones.forEach {
                mGoogleMap.drawCircle(
                    location = LatLng(it.latitude, it.longitude),
                    radius = it.radius,
                    fillColor = getResolvedColor(it.color.transparentColorId)
                )
            }
            mGoogleMap.centerWithLatLngList(
                resources,
                zones.map { LatLng(it.latitude, it.longitude) }
            )
        }
    }

    private fun showError(error: String) {
        appAlert {
            type = AlertType.Error
            message = error
        }
    }

    override fun onRefreshAdapter() {
        homeViewModel.getExclusionZones().getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> refreshView(it.data)
                is DataState.Error -> showError(it.error)
            }
        }
    }

    override fun onEditExclusionZone(zone: ExclusionZoneEntity) {
        AddExclusionZoneDialog.newEditingInstance(zone).setListener(this).show(childFragmentManager)
    }

    override fun onDeleteExclusionZone(id: Long) {
        homeViewModel.deleteExclusionZone(id).getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> appAlert {
                    type = AlertType.Success
                    message = getString(R.string.exclusion_zone_deleted_successfully)
                    positiveListener = {
                        close()
                        onRefreshAdapter()
                    }
                }
                is DataState.Error -> showError(it.error)
            }
        }
    }
}
