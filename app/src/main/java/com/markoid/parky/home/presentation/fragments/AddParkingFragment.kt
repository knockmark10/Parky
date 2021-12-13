package com.markoid.parky.home.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.core.presentation.dialogs.LoadingDialog
import com.markoid.parky.core.presentation.extensions.findMapById
import com.markoid.parky.core.presentation.extensions.show
import com.markoid.parky.core.presentation.extensions.toDouble
import com.markoid.parky.core.presentation.extensions.value
import com.markoid.parky.core.presentation.states.LoadingState
import com.markoid.parky.databinding.FragmentAddParkingBinding
import com.markoid.parky.home.domain.usecases.request.ValidateParkingRequest
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.home.presentation.enums.ParkingColor
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.presentation.extensions.setCameraPosition
import com.markoid.parky.position.presentation.extensions.setMarker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddParkingFragment : AbstractFragment<FragmentAddParkingBinding>() {

    private lateinit var mGoogleMap: GoogleMap

    private val homeViewModel by viewModels<HomeViewModel>()

    private val loadingDialog by lazy { LoadingDialog() }

    private val parkingRequest: ValidateParkingRequest
        get() = ValidateParkingRequest(
            address = binding.locationInfoContainer.locationAddressValue.value,
            latitude = binding.locationInfoContainer.locationLatitudeValue.value.toDouble(0.0),
            longitude = binding.locationInfoContainer.locationLongitudeValue.value.toDouble(0.0),
            parkingTime = binding.locationInfoContainer.parkingTimeValue.value,
            parkingType = binding.locationInfoContainer.parkingTypeValue.value,
            floorType = binding.locationLotInfoContainer.floorTypeValue.value,
            floorNumber = binding.locationLotInfoContainer.floorNumberValue.value,
            color = binding.locationLotInfoContainer.colorValue.value,
            lotIdentifier = binding.locationLotInfoContainer.lotIndentifierValue.value,
            fare = binding.locationLotInfoContainer.fareValue.value.toDouble(-0.1)
        )

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddParkingBinding = FragmentAddParkingBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        setupMap()

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.saveParkingBtn.setOnClickListener {
            homeViewModel.validateNewParking(parkingRequest)
                .getResult()
                .observe(viewLifecycleOwner, {
                    when (it) {
                        is DataState.Data -> handleValidationResult(it.data)
                        is DataState.Error -> showError(it.error)
                    }
                })
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
        response.getResult().observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Data -> onLocationReceived(it.data)
                is DataState.Error -> showError(it.error)
            }
        })
        response.getLoading().observe(viewLifecycleOwner, {
            when (it) {
                LoadingState.Show -> this.loadingDialog.show(childFragmentManager)
                LoadingState.Dismiss -> this.loadingDialog.dismiss()
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
        populateParkingFloor()
        populateColors()

        // Setting map information
        mGoogleMap.setCameraPosition(position.latLng, 18f)
        mGoogleMap.setMarker(requireContext(), position.latLng, R.drawable.ic_user_marker)
    }

    private fun populateColors() {
        val colorTypes: List<String> = ParkingColor.values().map { getString(it.colorId) }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            colorTypes
        )
        (binding.locationLotInfoContainer.colorContainer.editText as? AutoCompleteTextView?)?.apply {
            setAdapter(adapter)
        }
    }

    private fun populateParkingFloor() {
        val types: List<String> = ParkingFloorType.values().map { getString(it.typeId) }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            types
        )
        (binding.locationLotInfoContainer.floorTypeContainer.editText as? AutoCompleteTextView?)?.apply {
            setAdapter(adapter)
        }
    }

    private fun populateParkingTypes() {
        val parkingTypes: List<String> = ParkingType.values().map { getString(it.typeId) }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            parkingTypes
        )
        (binding.locationInfoContainer.parkingTypeContainer.editText as? AutoCompleteTextView?)?.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                binding.locationLotInfoContainer.root.isVisible =
                    parkingTypes[position] == getString(ParkingType.ParkingLot.typeId)
            }
        }
    }

    private fun showError(error: String) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
    }
}
