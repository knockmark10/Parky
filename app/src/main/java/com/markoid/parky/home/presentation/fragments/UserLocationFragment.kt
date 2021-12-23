package com.markoid.parky.home.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.* // ktlint-disable no-wildcard-imports
import com.markoid.parky.databinding.FragmentUserLocationBinding
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.extensions.getAlarmTimeFormatted
import com.markoid.parky.home.data.extensions.latLng
import com.markoid.parky.home.domain.usecases.response.LocationUpdatesResponse
import com.markoid.parky.home.presentation.AlarmDialog
import com.markoid.parky.home.presentation.CarPhotoDialog
import com.markoid.parky.home.presentation.fragments.ParkingHistoryFragment.Companion.SPOT_ID
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel
import com.markoid.parky.position.presentation.extensions.centerWithLatLngList
import com.markoid.parky.position.presentation.extensions.setMarker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UserLocationFragment : AbstractFragment<FragmentUserLocationBinding>() {

    private val parkingSpotId: Long
        get() = arguments?.getLong(SPOT_ID) ?: 0L

    private lateinit var mGoogleMap: GoogleMap

    private val viewModel by viewModels<HomeViewModel>()

    private var parkingSpot: ParkingSpotEntity? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserLocationBinding = FragmentUserLocationBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        viewModel.startObservingLifecycle(viewLifecycleOwner.lifecycle)

        setupMap()

        setClickListener()
    }

    private fun setClickListener() {
        binding.actionDelete.setOnClickListener { displayAlertToDeleteParkingSpot() }
        binding.actionAlarm.setOnClickListener { displayAlarmInfo() }
        binding.actionCamera.setOnClickListener { displayCarPhoto() }
        binding.actionRate.setOnClickListener { displayRate() }
        binding.actionCollapse.setOnClickListener { collapseCard() }
    }

    private fun deleteParkingSpot() {
        viewModel.deleteParkingSpot(parkingSpotId).getResult().react(viewLifecycleOwner) {
            when (this) {
                is DataState.Data -> displayParkingSpotDeletedSuccessfully()
                is DataState.Error -> showError(error)
            }
        }
    }

    private fun displayParkingSpotDeletedSuccessfully() {
        appAlert {
            type = AlertType.Success
            message = getString(R.string.parking_spot_deleted_successful_message)
            positiveListener = {
                close()
                requireActivity().onBackPressed()
            }
        }
    }

    private fun showError(error: String) {
        appAlert {
            message = error
            type = AlertType.Error
        }
    }

    private fun displayAlertToDeleteParkingSpot() {
        appAlert {
            message = getString(R.string.parking_spot_delete_title)
            type = AlertType.Warning
            positiveListener = {
                deleteParkingSpot()
                close()
            }
            negativeListener = { close() }
        }
    }

    // https://medium.com/android-news/using-alarmmanager-like-a-pro-20f89f4ca720
    private fun displayAlarmInfo() {
        parkingSpot?.alarmTime?.let { AlarmDialog.newInstance(it).show(childFragmentManager) }
    }

    private fun displayCarPhoto() {
        parkingSpot?.photo?.let {
            CarPhotoDialog.newInstance(it).show(childFragmentManager, this::class.java.name)
        }
    }

    private fun displayRate() {
    }

    private fun collapseCard() {
        val degrees: Float = if (binding.locationExpandable.isExpanded) 180f else 0f
        binding.actionCollapse.rotation = degrees
        binding.locationExpandable.toggle()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findMapById(R.id.user_location_map)
        mapFragment?.getMapAsync { handleMap(it) }
    }

    @SuppressLint("MissingPermission")
    private fun handleMap(map: GoogleMap) {
        this.mGoogleMap = map
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        map.isMyLocationEnabled = true
        getActiveParkingSpot()
    }

    private fun getActiveParkingSpot() {
        viewModel.getActiveParkingSpot().getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> setCardInformation(it.data)
                is DataState.Error ->
                    Snackbar
                        .make(binding.root, it.error, Snackbar.LENGTH_LONG)
                        .show()
            }
        }
    }

    private fun setCardInformation(parkingSpot: ParkingSpotEntity) {
        // Save global variable
        this.parkingSpot = parkingSpot

        // Displaying parking spot marker
        mGoogleMap.setMarker(requireContext(), parkingSpot.latLng, R.drawable.ic_parking_marker)

        // Setting up card information
        binding.actionRate.isVisible = parkingSpot.fare >= 0.0
        binding.actionAlarm.isVisible = parkingSpot.alarmTime != null
        binding.actionCamera.isVisible = parkingSpot.photo != null
        binding.carAddress.text = parkingSpot.address
        binding.alarmTime.text = parkingSpot.getAlarmTimeFormatted()
        getUserLocation(parkingSpot.latLng)
    }

    private fun getUserLocation(parkingSpotLocation: LatLng) {
        viewLifecycleOwner.lifecycleScope.launchWhenStartedCatching(
            lifecycle,
            { observeUserLocationUpdates(parkingSpotLocation) },
            { it.printStackTrace() }
        )
    }

    private fun CoroutineScope.observeUserLocationUpdates(parkingSpotLocation: LatLng) {
        viewModel.getUserLocationUpdates(parkingSpotLocation)
            .onEach { setRealTimeData(it) }
            .launchIn(this)
    }

    private fun setRealTimeData(data: LocationUpdatesResponse) {
        binding.time.text = data.time
        binding.distance.text = data.distance
        binding.speed.text = data.speedKph
        val locationList: List<LatLng> = parkingSpot?.let { listOf(it.latLng, data.userLocation) }
            ?: listOf(data.userLocation)
        mGoogleMap.centerWithLatLngList(resources, locationList)
    }
}