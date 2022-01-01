package com.markoid.parky.home.presentation.fragments

import androidx.core.view.isVisible
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.dialogs.LoadingDialog
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.appAlert
import com.markoid.parky.core.presentation.extensions.ensureAdded
import com.markoid.parky.core.presentation.extensions.show
import com.markoid.parky.core.presentation.extensions.subscribe
import com.markoid.parky.core.presentation.states.LoadingState
import com.markoid.parky.home.presentation.enums.ParkingType
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddParkingFragment : ParkingFormBaseFragment() {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    private val parkingType: ParkingType
        get() = ParkingType.forValue(devicePreferences.favoriteParkingType)
            ?: ParkingType.StreetParking

    private val loadingDialog by lazy { LoadingDialog() }

    override fun onGetCurrentLocation() = ensureAdded {
        val response = homeViewModel.getCurrentLocation()
        response.getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> onLocationReceived(it.data)
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

    override fun onSaveParkingSpot() {
        val response = homeViewModel.saveParkingSpot(parkingRequest)
        response.getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> handleValidationResult(it.data)
                is DataState.Error -> showError(it.error, false)
            }
        }
        response.getLoading().subscribe(viewLifecycleOwner) {
            when (it) {
                LoadingState.Show -> loadingDialog.show(childFragmentManager)
                LoadingState.Dismiss -> loadingDialog.dismiss()
            }
        }
    }

    override fun onUpdateParkingSpot() {
        val response = homeViewModel.updateParkingSpot(parkingRequest)
        response.getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> handleValidationResult(it.data)
                is DataState.Error -> showError(it.error, false)
            }
        }
        response.getLoading().subscribe(viewLifecycleOwner) {
            when (it) {
                LoadingState.Show -> loadingDialog.show(childFragmentManager)
                LoadingState.Dismiss -> loadingDialog.dismiss()
            }
        }
    }

    override fun onTakePhoto() = ensureAdded {
        homeViewModel.takeCarPicture().getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> displayCarImage(it.data)
                is DataState.Error -> handlePictureError(it.error)
            }
        }
    }

    private fun onLocationReceived(position: PositionEntity) {
        updateParkingTime(position.dateTime)
        setAddressInformation(position)
        displayMarkerOnMap(position.latLng)
    }

    private fun setAddressInformation(position: PositionEntity) {
        binding.locationInfoContainer.apply {
            locationAddressValue.setText(position.streetAddress)
            locationLatitudeValue.setText(position.latitude.toString())
            locationLongitudeValue.setText(position.longitude.toString())
            parkingTimeValue.setText(position.dateFormatted)
            parkingTypeValue.setText(getString(parkingType.typeId))
        }
    }

    private fun showError(error: String, exit: Boolean = true) {
        appAlert {
            message = error
            type = AlertType.Error
            positiveListener = {
                close()
                if (exit) requireActivity().onBackPressed()
            }
        }
    }

    private fun handlePictureError(error: String) {
        binding.carPictureContainer.root.isVisible = false
        showError(error, false)
    }
}
