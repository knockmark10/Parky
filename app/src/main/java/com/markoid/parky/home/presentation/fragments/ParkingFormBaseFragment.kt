package com.markoid.parky.home.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.R
import com.markoid.parky.core.date.enums.FormatType
import com.markoid.parky.core.date.formatters.DateTimeFormatter
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.* // ktlint-disable no-wildcard-imports
import com.markoid.parky.databinding.FragmentAddParkingBinding
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.home.presentation.enums.ParkingColor
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType
import com.markoid.parky.home.presentation.utils.AlarmUtils
import com.markoid.parky.position.presentation.extensions.setCameraPosition
import com.markoid.parky.position.presentation.extensions.setMarker
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.joda.time.DateTime

abstract class ParkingFormBaseFragment : HomeBaseFragment<FragmentAddParkingBinding>() {

    val parkingRequest: ParkingSpotRequest
        get() = ParkingSpotRequest(
            address = binding.locationInfoContainer.locationAddressValue.value,
            alarmTime = alarmTime,
            color = binding.locationLotInfoContainer.colorValue.value,
            hourRate = binding.locationLotInfoContainer.hourRateValue.value.toDouble(-0.1),
            floorNumber = binding.locationLotInfoContainer.floorNumberValue.value,
            floorType = binding.locationLotInfoContainer.floorTypeValue.value,
            latitude = binding.locationInfoContainer.locationLatitudeValue.value.toDouble(0.0),
            longitude = binding.locationInfoContainer.locationLongitudeValue.value.toDouble(0.0),
            lotIdentifier = binding.locationLotInfoContainer.lotIndentifierValue.value,
            parkingTime = parkingTime,
            parkingTimeFormatted = binding.locationInfoContainer.parkingTimeValue.value,
            parkingType = binding.locationInfoContainer.parkingTypeValue.value,
            photo = carPhotoUri
        )

    private lateinit var mGoogleMap: GoogleMap

    private val navigationArgs: AddParkingFragmentArgs by navArgs()

    private var parkingTime: DateTime = DateTime.now()

    private var alarmTime: DateTime? = null

    private var carPhotoUri: Uri? = null

    private val isEditMode: Boolean
        get() = navigationArgs.editMode

    private val incompleteRequest: ParkingSpotRequest?
        get() = navigationArgs.parkingSpotRequest

    private val initialDelay: Long
        get() = if (incompleteRequest == null) 1500L else 0L

    abstract fun onSaveParkingSpot()

    abstract fun onUpdateParkingSpot()

    abstract fun onTakePhoto()

    abstract fun onGetCurrentLocation()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddParkingBinding = FragmentAddParkingBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        setupView()

        setInitialDataWithDelay()
    }

    fun displayCarImage(uri: Uri) {
        this.carPhotoUri = uri
        binding.carPictureContainer.root.isVisible = true
        binding.carPictureContainer.vehiclePicture.setImageURI(uri)
    }

    fun displayAlarmDialog() {
        dateAndTimePickers(true, {
            alarmTime = this
            binding.locationInfoContainer.parkingAlarmContainer.isVisible = true
            binding
                .locationInfoContainer
                .parkingAlarmValue
                .setText(DateTimeFormatter.format(FormatType.MONTH_DAY_YEAR_HOUR, this))
        }, {
            longToast("Alarm was not set")
        })
    }

    fun handleValidationResult(result: ParkingValidationStatus) {
        clearErrors()
        when (result) {
            is ParkingValidationStatus.Failure.EmptyAddress ->
                binding.locationInfoContainer.locationAddressContainer.error = result.message
            is ParkingValidationStatus.Failure.EmptyColor ->
                binding.locationLotInfoContainer.colorContainer.error = result.message
            is ParkingValidationStatus.Failure.EmptyFloorNumber ->
                binding.locationLotInfoContainer.floorNumberContainer.error = result.message
            is ParkingValidationStatus.Failure.InvalidFare ->
                binding.locationLotInfoContainer.hourRateContainer.error = result.message
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
            is ParkingValidationStatus.Success -> handleValidationsSucceeded()
        }
    }

    fun updateParkingTime(dateTime: DateTime) {
        parkingTime = dateTime
    }

    fun displayMarkerOnMap(position: LatLng) {
        mGoogleMap.setCameraPosition(position, 18f)
        mGoogleMap.setMarker(requireContext(), position, R.drawable.ic_parking_marker)
    }

    private fun setupView() {
        binding.parkingBtn.text = if (isEditMode) getString(R.string.update_parking_lot)
        else getString(R.string.save_parking_lot)
        populateColors()
        populateParkingFloor()
        populateParkingTypes()
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

    private fun fillIncompleteRequestData(request: ParkingSpotRequest) {
        binding.locationInfoContainer.locationAddressValue.setText(request.address)
        binding.locationInfoContainer.locationLatitudeValue.setText(request.latitude.toString())
        binding.locationInfoContainer.locationLongitudeValue.setText(request.longitude.toString())
        binding.locationInfoContainer.parkingTimeValue.setText(request.parkingTimeFormatted)
        request.alarmTime?.let {
            alarmTime = it
            binding.locationInfoContainer.parkingAlarmContainer.isVisible = true
            binding
                .locationInfoContainer
                .parkingAlarmValue
                .setText(DateTimeFormatter.format(FormatType.MONTH_DAY_YEAR_HOUR, it))
        }
        binding.locationLotInfoContainer.root.isVisible =
            ParkingType.forValue(resources, request.parkingType) == ParkingType.ParkingLot
        binding.locationInfoContainer.parkingTypeValue.setText(request.parkingType)
        binding.locationLotInfoContainer.hourRateValue.setText(request.hourRate.toString())
        displayMarkerOnMap(LatLng(request.latitude, request.longitude))
    }

    private fun setInitialDataWithDelay(): Job = lifecycleScope.launchWhenStarted {
        delay(initialDelay)
        setupMap()
        setClickListeners()
    }

    private fun scheduleAlarm() {
        alarmTime?.let { AlarmUtils.setAlarm(requireContext(), it) }
    }

    private fun setupMap() =
        childFragmentManager.findMapById(R.id.add_parking_map_view)?.getMapAsync {
            this.mGoogleMap = it
            it.mapType = GoogleMap.MAP_TYPE_HYBRID
            incompleteRequest?.let { request -> fillIncompleteRequestData(request) }
                ?: onGetCurrentLocation()
        }

    private fun handleValidationsSucceeded() {
        // Schedule alarm upon validations success
        scheduleAlarm()

        // Display success alert
        val resId: Int = if (isEditMode) R.string.parking_spot_updated_successfully
        else R.string.parking_spot_saved_successfully

        appAlert {
            type = AlertType.Success
            message = getString(resId)
            positiveListener = {
                navigationListener?.onUpdateDrawerMenuItemVisibility(
                    R.id.home_add_parking,
                    false
                )
                navigationListener?.onUpdateDrawerMenuItemVisibility(
                    R.id.home_user_location,
                    true
                )
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setClickListeners() {
        binding.parkingBtn.setOnClickListener {
            if (isEditMode) onUpdateParkingSpot()
            else onSaveParkingSpot()
        }
        binding.carPictureContainer.editBtn.setOnClickListener { onTakePhoto() }
    }

    private fun clearErrors() {
        binding.locationInfoContainer.locationAddressContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.colorContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.floorNumberContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.hourRateContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.floorTypeContainer.isErrorEnabled = false
        binding.locationInfoContainer.locationLatitudeContainer.isErrorEnabled = false
        binding.locationInfoContainer.locationLongitudeContainer.isErrorEnabled = false
        binding.locationLotInfoContainer.lotIdentifierContainer.isErrorEnabled = false
        binding.locationInfoContainer.parkingTimeContainer.isErrorEnabled = false
        binding.locationInfoContainer.parkingTypeContainer.isErrorEnabled = false
    }

    private fun List<String>.buildArrayAdapter(): ArrayAdapter<String> = ArrayAdapter(
        requireContext(),
        android.R.layout.simple_expandable_list_item_1,
        this
    )
}
