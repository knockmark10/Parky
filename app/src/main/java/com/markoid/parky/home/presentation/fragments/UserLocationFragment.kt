package com.markoid.parky.home.presentation.fragments

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
import com.markoid.parky.core.presentation.extensions.findMapById
import com.markoid.parky.core.presentation.extensions.latLng
import com.markoid.parky.core.presentation.extensions.launchWhenStartedCatching
import com.markoid.parky.core.presentation.extensions.subscribe
import com.markoid.parky.databinding.FragmentUserLocationBinding
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.domain.usecases.response.LocationUpdatesResponse
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel
import com.markoid.parky.position.presentation.extensions.setCameraPosition
import com.markoid.parky.position.presentation.extensions.setMarker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UserLocationFragment : AbstractFragment<FragmentUserLocationBinding>() {

    private lateinit var mGoogleMap: GoogleMap

    private val viewModel by viewModels<HomeViewModel>()

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
        binding.actionCollapse.setOnClickListener {
            val degrees: Float = if (binding.locationExpandable.isExpanded) 180f else 0f
            binding.actionCollapse.rotation = degrees
            binding.locationExpandable.toggle()
        }
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findMapById(R.id.user_location_map)
        mapFragment?.getMapAsync { handleMap(it) }
    }

    private fun handleMap(map: GoogleMap) {
        this.mGoogleMap = map
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
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
        // Setting up map
        mGoogleMap.setMarker(
            requireContext(),
            parkingSpot.latLng,
            R.drawable.ic_user_marker
        )

        // Setting up card information
        binding.actionRate.isVisible = parkingSpot.fare >= 0.0
        binding.actionAlarm.isVisible = false
        binding.actionCamera.isVisible = parkingSpot.photo != null
        binding.carAddress.text = parkingSpot.address
        getUserLocation(parkingSpot.latLng)

        /*mGoogleMap.isMyLocationEnabled = true
        mGoogleMap.setCameraPosition(userLocation, 18f)*/
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
        mGoogleMap.setCameraPosition(data.userLocation, 18f)
    }
}
