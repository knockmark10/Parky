package com.markoid.parky.home.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.core.presentation.extensions.findMapById
import com.markoid.parky.core.presentation.states.LoadingState
import com.markoid.parky.databinding.FragmentAddParkingBinding
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.presentation.extensions.setCameraPosition
import com.markoid.parky.position.presentation.extensions.setMarker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddParkingFragment : AbstractFragment<FragmentAddParkingBinding>() {

    private lateinit var mGoogleMap: GoogleMap

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddParkingBinding = FragmentAddParkingBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        setupMap()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findMapById(R.id.add_parking_map_view)
        mapFragment?.getMapAsync { handleMap(it) }
    }

    private fun handleMap(map: GoogleMap) {
        this.mGoogleMap = map
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        val response = homeViewModel.getCurrentLocation()
        response.getResult().observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Data -> onLocationReceived(it.data)
                is DataState.Error -> Log.e("ERROR", it.error)
            }
        })
        response.getLoading().observe(viewLifecycleOwner, {
            when (it) {
                LoadingState.Show -> Log.d("loading", "Show loading")
                LoadingState.Dismiss -> Log.d("loading", "Dismiss loading")
            }
        })
    }

    private fun onLocationReceived(position: PositionEntity) {
        // Setting address information
        binding.locationInfoContainer.apply {
            locationAddressValue.setText(position.streetAddress)
            locationLatitudeValue.setText(position.latitude.toString())
            locationLongitudeValue.setText(position.longitude.toString())
            parkingTimeValue.setText(position.dateFormatted)
            populateParkingTypes()
        }

        // Setting parking lot information
        val types = listOf("Floor", "Basement")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            types
        )
        (binding.locationLotInfoContainer.floorTypeContainer.editText as? AutoCompleteTextView?)?.apply {
            setAdapter(adapter)
        }

        // Setting map information
        mGoogleMap.setCameraPosition(position.latLng, 18f)
        mGoogleMap.setMarker(requireContext(), position.latLng, R.drawable.ic_user_marker)
    }

    private fun populateParkingTypes() {
        val parkingTypes = listOf("Street-Parking", "Parking-Lot")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            parkingTypes
        )
        (binding.locationInfoContainer.parkingTypeContainer.editText as? AutoCompleteTextView?)?.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                binding.locationLotInfoContainer.root.isVisible =
                    parkingTypes[position] == "Parking-Lot"
            }
        }
    }
}
