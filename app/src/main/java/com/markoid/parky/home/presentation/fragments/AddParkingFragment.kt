package com.markoid.parky.home.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.core.presentation.dialogs.LoadingDialog
import com.markoid.parky.core.presentation.extensions.* // ktlint-disable no-wildcard-imports
import com.markoid.parky.core.presentation.states.LoadingState
import com.markoid.parky.databinding.FragmentAddParkingBinding
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.home.presentation.enums.ParkingColor
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.presentation.extensions.setCameraPosition
import com.markoid.parky.position.presentation.extensions.setMarker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import org.joda.time.DateTime

@AndroidEntryPoint
class AddParkingFragment : AbstractFragment<FragmentAddParkingBinding>() {

    private lateinit var mGoogleMap: GoogleMap

    private val homeViewModel by viewModels<HomeViewModel>()

    private val loadingDialog by lazy { LoadingDialog() }

    private var parkingTime: DateTime = DateTime.now()

    private val parkingRequest: ParkingSpotRequest
        get() = ParkingSpotRequest(
            address = binding.locationInfoContainer.locationAddressValue.value,
            color = binding.locationLotInfoContainer.colorValue.value,
            fare = binding.locationLotInfoContainer.fareValue.value.toDouble(-0.1),
            floorNumber = binding.locationLotInfoContainer.floorNumberValue.value,
            floorType = binding.locationLotInfoContainer.floorTypeValue.value,
            latitude = binding.locationInfoContainer.locationLatitudeValue.value.toDouble(0.0),
            longitude = binding.locationInfoContainer.locationLongitudeValue.value.toDouble(0.0),
            lotIdentifier = binding.locationLotInfoContainer.lotIndentifierValue.value,
            parkingTime = parkingTime,
            parkingTimeFormatted = binding.locationInfoContainer.parkingTimeValue.value,
            parkingType = binding.locationInfoContainer.parkingTypeValue.value
        )

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddParkingBinding = FragmentAddParkingBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenStarted {
            delay(1000L)
            setupMap()
            setClickListeners()
        }
    }

    private fun setClickListeners() {
        binding.saveParkingBtn.setOnClickListener { saveParkingSpot() }
    }

    private fun saveParkingSpot() {
        val response = homeViewModel.saveParkingSpot(parkingRequest)
        response.getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> handleValidationResult(it.data)
                is DataState.Error -> showError(it.error)
            }
        }
        response.getLoading().subscribe(viewLifecycleOwner) {
            when (it) {
                LoadingState.Show -> loadingDialog.show(childFragmentManager)
                LoadingState.Dismiss -> loadingDialog.dismiss()
            }
        }
    }

    private fun handleValidationResult(result: ParkingValidationStatus) {
        clearErrors()
        when (result) {
            is ParkingValidationStatus.Failure.EmptyAddress ->
                binding.locationInfoContainer.locationAddressContainer.error = result.message
            is ParkingValidationStatus.Failure.EmptyColor ->
                binding.locationLotInfoContainer.colorContainer.error = result.message
            is ParkingValidationStatus.Failure.EmptyFloorNumber ->
                binding.locationLotInfoContainer.floorNumberContainer.error = result.message
            is ParkingValidationStatus.Failure.InvalidFare ->
                binding.locationLotInfoContainer.fareContainer.error = result.message
            is ParkingValidationStatus.Failure.InvalidFloorType ->
                binding.locationLotInfoContainer.floorTypeContainer.error = result.message
            is ParkingValidationStatus.Failure.InvalidLocation -> {
                binding.locationInfoContainer.locationLatitudeContainer.error = result.message
                binding.locationInfoContainer.locationLongitudeContainer.error = result.message
            }
            is ParkingValidationStatus.Failure.InvalidLotIdentifier ->
                binding.locationLotInfoContainer.lotIdentifierContainer.error = result.message
            is ParkingValidationStatus.Failure.InvalidParkingTime ->
                binding.locationInfoContainer.parkingTimeContainer.error = result.message
            is ParkingValidationStatus.Failure.InvalidParkingType ->
                binding.locationInfoContainer.parkingTypeContainer.error = result.message
            ParkingValidationStatus.Success -> showError("Nice dick, bro!")
        }
    }

    private fun clearErrors() {
        binding.locationInfoContainer.locationAddressContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.colorContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.floorNumberContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.fareContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.floorTypeContainer.isErrorEnabled = false
        binding.locationInfoContainer.locationLatitudeContainer.isErrorEnabled = false
        binding.locationInfoContainer.locationLongitudeContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.lotIdentifierContainer.isErrorEnabled = false
        binding.locationInfoContainer.parkingTimeContainer.isErrorEnabled = false
        binding.locationInfoContainer.parkingTypeContainer.isErrorEnabled = false
    }

    private fun setupMap() =
        childFragmentManager.findMapById(R.id.add_parking_map_view)?.getMapAsync { handleMap(it) }

    private fun handleMap(map: GoogleMap) {
        this.mGoogleMap = map
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        val response = homeViewModel.getCurrentLocation()
        response.getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> onLocationReceived(it.data)
                is DataState.Error -> showError(it.error)
            }
        }
        response.getLoading().subscribe(viewLifecycleOwner) {
            when (it) {
                LoadingState.Show -> this.loadingDialog.show(childFragmentManager)
                LoadingState.Dismiss -> this.loadingDialog.dismiss()
            }
        }
    }

    private fun onLocationReceived(position: PositionEntity) {
        this.parkingTime = position.dateTime
        // Setting address information
        binding.locationInfoContainer.apply {
            locationAddressValue.setText(position.streetAddress)
            locationLatitudeValue.setText(position.latitude.toString())
            locationLongitudeValue.setText(position.longitude.toString())
            parkingTimeValue.setText(position.dateFormatted)
            populateParkingTypes()
        }

        // Setting parking lot information
        populateParkingFloor()
        populateColors()

        // Setting map information
        mGoogleMap.setCameraPosition(position.latLng, 18f)
        mGoogleMap.setMarker(requireContext(), position.latLng, R.drawable.ic_user_marker)
    }

    private fun populateColors() {
        (binding.locationLotInfoContainer.colorContainer.editText as? AutoCompleteTextView?)
            ?.setAdapter(
                ParkingColor
                    .values()
                    .map { getString(it.colorId) }
                    .buildArrayAdapter()
            )
    }

    private fun populateParkingFloor() {
        (binding.locationLotInfoContainer.floorTypeContainer.editText as? AutoCompleteTextView?)
            ?.setAdapter(
                ParkingFloorType
                    .values()
                    .map { getString(it.typeId) }
                    .buildArrayAdapter()
            )
    }

    private fun populateParkingTypes() {
        val parkingTypes: List<String> = ParkingType.values().map { getString(it.typeId) }
        val adapter = parkingTypes.buildArrayAdapter()
        (binding.locationInfoContainer.parkingTypeContainer.editText as? AutoCompleteTextView?)?.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                binding.locationLotInfoContainer.root.isVisible =
                    parkingTypes[position] == getString(ParkingType.ParkingLot.typeId)
            }
        }
    }

    private fun List<String>.buildArrayAdapter(): ArrayAdapter<String> = ArrayAdapter(
        requireContext(),
        android.R.layout.simple_expandable_list_item_1,
        this
    )

    private fun showError(error: String) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        requireActivity().onBackPressed()
    }
}
