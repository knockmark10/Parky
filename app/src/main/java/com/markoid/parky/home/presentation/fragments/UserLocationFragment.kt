package com.markoid.parky.home.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.appAlert
import com.markoid.parky.core.presentation.extensions.appAlertDelegate
import com.markoid.parky.core.presentation.extensions.ensureAdded
import com.markoid.parky.core.presentation.extensions.findMapById
import com.markoid.parky.core.presentation.extensions.launchWhenStartedCatching
import com.markoid.parky.core.presentation.extensions.react
import com.markoid.parky.core.presentation.extensions.show
import com.markoid.parky.core.presentation.extensions.subscribe
import com.markoid.parky.databinding.FragmentUserLocationBinding
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.extensions.getAlarmTimeFormatted
import com.markoid.parky.home.data.extensions.latLng
import com.markoid.parky.home.domain.usecases.response.LocationUpdatesResponse
import com.markoid.parky.home.presentation.dialgos.AlarmDialog
import com.markoid.parky.home.presentation.dialgos.CarPhotoDialog
import com.markoid.parky.home.presentation.dialgos.MapTypeDialog
import com.markoid.parky.home.presentation.dialgos.RateDialog
import com.markoid.parky.position.presentation.extensions.centerWithLatLngList
import com.markoid.parky.position.presentation.extensions.setDarkMode
import com.markoid.parky.position.presentation.extensions.setMarker
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class UserLocationFragment : HomeBaseFragment<FragmentUserLocationBinding>() {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    private val navigationArgs: UserLocationFragmentArgs by navArgs()

    private val parkingSpotId: Long
        get() = navigationArgs.spotId

    private var mGoogleMap: GoogleMap? = null

    private var parkingSpot: ParkingSpotEntity? = null

    private var hasCarFoundDialogBeenDisplayed: Boolean = false

    private val carFoundDialog by appAlertDelegate {
        type = AlertType.Info
        message = getString(R.string.add_parking_car_found_message)
        positiveListener = { archiveParking() }
        negativeListener = { close() }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserLocationBinding = FragmentUserLocationBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        navigationListener?.onSetBackArrowOnToolbar()

        setupMap()

        setClickListener()
    }

    private fun setClickListener() {
        binding.actionDelete.setOnClickListener { displayAlertToDeleteParkingSpot() }
        binding.actionAlarm.setOnClickListener { displayAlarmInfo() }
        binding.actionCamera.setOnClickListener { displayCarPhoto() }
        binding.actionRate.setOnClickListener { displayRate() }
        binding.actionCollapse.setOnClickListener { collapseCard() }
        binding.navigationBtn.setOnClickListener { startMapsNavigation() }
    }

    private fun deleteParkingSpot() {
        homeViewModel.deleteParkingSpot(parkingSpotId).getResult().react(viewLifecycleOwner) {
            when (this) {
                is DataState.Data -> displayParkingSpotDeletedSuccessfully()
                is DataState.Error -> showError(error)
            }
        }
    }

    private fun displayParkingSpotDeletedSuccessfully(): Unit = appAlert {
        type = AlertType.Success
        message = getString(R.string.parking_spot_deleted_successful_message)
        positiveListener = {
            close()
            requireActivity().onBackPressed()
        }
    }

    private fun showError(error: String): Unit = appAlert {
        message = error
        type = AlertType.Error
    }

    private fun changeMapType(type: Int) {
        mGoogleMap?.mapType = type
        if (devicePreferences.isDarkModeEnabled) mGoogleMap?.setDarkMode(requireContext())
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

    private fun displayAlarmInfo() {
        parkingSpot?.alarmTime?.let { AlarmDialog.newInstance(it).show(childFragmentManager) }
    }

    private fun displayCarPhoto() {
        parkingSpot?.photo?.let {
            CarPhotoDialog.newInstance(it).show(childFragmentManager, this::class.java.name)
        }
    }

    private fun displayRate() {
        RateDialog().show(childFragmentManager)
    }

    private fun collapseCard() {
        val degrees: Float = if (binding.locationExpandable.isExpanded) 180f else 0f
        binding.actionCollapse.rotation = degrees
        binding.locationExpandable.toggle()
    }

    private fun startMapsNavigation() {
        parkingSpot?.let {
            // Create a Uri from an intent string. Use the result to create an Intent.
            val gmmIntentUri = Uri.parse("google.navigation:q=${it.latitude},${it.longitude}")
            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")
            // Attempt to start an activity that can handle the Intent
            mapIntent.resolveActivity(requireContext().packageManager)
                ?.let { startActivity(mapIntent) }
        }
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findMapById(R.id.user_location_map)
        mapFragment?.getMapAsync { handleMap(it) }
    }

    @SuppressLint("MissingPermission")
    private fun handleMap(map: GoogleMap) {
        this.mGoogleMap = map
        map.isMyLocationEnabled = true
        changeMapType(devicePreferences.mapType)
        getActiveParkingSpot()
    }

    private fun getActiveParkingSpot() {
        homeViewModel.getActiveParkingSpot().getResult().subscribe(viewLifecycleOwner) {
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
        mGoogleMap?.setMarker(requireContext(), parkingSpot.latLng, R.drawable.ic_parking_marker)

        // Setting up card information
        binding.actionRate.isVisible = parkingSpot.hourRate >= 0.0
        binding.actionAlarm.isVisible = parkingSpot.alarmTime != null
        binding.actionCamera.isVisible = parkingSpot.photo != null
        binding.carAddress.text = parkingSpot.address
        binding.alarmTime.isVisible = parkingSpot.alarmTime != null
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
        homeViewModel.getLocationUpdatesWithSpotDistance(parkingSpotLocation)
            .onEach { setRealTimeData(it) }
            .launchIn(this)
    }

    private fun setRealTimeData(data: LocationUpdatesResponse) {
        binding.time.text = data.time
        binding.distance.text = data.distanceWithDisplayFormat
        binding.speed.text = data.speedKph
        val locationList: List<LatLng> = parkingSpot?.let { listOf(it.latLng, data.userLocation) }
            ?: listOf(data.userLocation)
        mGoogleMap?.centerWithLatLngList(resources, locationList)
        checkDistanceToFinishParking(data.distance)
    }

    private fun archiveParking() {
        homeViewModel.finishParking(parkingSpotId).getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> appAlert {
                    type = AlertType.Success
                    message = getString(R.string.parking_spot_archived_successfully)
                    positiveListener = {
                        close()
                        requireActivity().onBackPressed()
                    }
                }
                is DataState.Error -> showError(it.error)
            }
        }
    }

    private fun checkDistanceToFinishParking(distance: Double) = ensureAdded {
        if (distance < 5.0 &&
            carFoundDialog.isDialogShowing.not() &&
            hasCarFoundDialogBeenDisplayed.not()
        ) {
            carFoundDialog.show(childFragmentManager)
            hasCarFoundDialogBeenDisplayed = true
        }
    }

    fun displayMapTypeDialog() = ensureAdded {
        MapTypeDialog()
            .setOnMapTypeSelectedListener { changeMapType(it) }
            .show(childFragmentManager)
    }

    fun finishParking() = ensureAdded {
        appAlert {
            type = AlertType.Info
            message = getString(R.string.parking_spot_finish)
            positiveListener = {
                archiveParking()
                close()
            }
            negativeListener = { close() }
        }
    }
}
