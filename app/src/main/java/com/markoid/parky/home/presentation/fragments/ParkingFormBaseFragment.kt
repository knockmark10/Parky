package com.markoid.parky.home.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.R
import com.markoid.parky.core.date.enums.FormatType
import com.markoid.parky.core.date.extensions.formatWith
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.appAlert
import com.markoid.parky.core.presentation.extensions.dateAndTimePickers
import com.markoid.parky.core.presentation.extensions.disableScrolling
import com.markoid.parky.core.presentation.extensions.ensureAdded
import com.markoid.parky.core.presentation.extensions.findScrollingMapById
import com.markoid.parky.core.presentation.extensions.longToast
import com.markoid.parky.core.presentation.extensions.toDouble
import com.markoid.parky.core.presentation.extensions.value
import com.markoid.parky.core.presentation.views.buildArrayAdapter
import com.markoid.parky.databinding.FragmentAddParkingBinding
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.extensions.latLng
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.home.presentation.enums.ParkingColor
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType
import com.markoid.parky.home.presentation.enums.ParkingType.ParkingLot
import com.markoid.parky.home.presentation.enums.ParkingType.values
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
            color = parkingColor?.name.orEmpty(),
            hourRate = binding.locationLotInfoContainer.hourRateValue.value.toDouble(-0.1),
            floorNumber = binding.locationLotInfoContainer.floorNumberValue.value,
            floorType = parkingFloorType?.name.orEmpty(),
            id = parkingSpotToEdit?.id,
            latitude = binding.locationInfoContainer.locationLatitudeValue.value.toDouble(0.0),
            longitude = binding.locationInfoContainer.locationLongitudeValue.value.toDouble(0.0),
            lotIdentifier = binding.locationLotInfoContainer.lotIndentifierValue.value,
            parkingTime = parkingTime,
            parkingTimeFormatted = binding.locationInfoContainer.parkingTimeValue.value,
            parkingType = parkingType?.name.orEmpty(),
            photo = carPhotoUri
        )

    private val parkingType: ParkingType?
        get() = ParkingType.fromLocalizedValue(resources, parkingTypeFieldValue)

    private val parkingFloorType: ParkingFloorType?
        get() = ParkingFloorType.fromLocalizedValue(resources, parkingFloorTypeFieldValue)

    private val parkingColor: ParkingColor?
        get() = ParkingColor.fromLocalizedValue(resources, parkingColorFieldValue)

    private val parkingTypeFieldValue: String
        get() = binding.locationInfoContainer.parkingTypeValue.value

    private val parkingFloorTypeFieldValue: String
        get() = binding.locationLotInfoContainer.floorTypeValue.value

    private val parkingColorFieldValue: String
        get() = binding.locationLotInfoContainer.colorValue.value

    private lateinit var mGoogleMap: GoogleMap

    private val navigationArgs: AddParkingFragmentArgs by navArgs()

    private var parkingTime: DateTime = DateTime.now()

    private var alarmTime: DateTime? = null

    private var carPhotoUri: Uri? = null

    private val parkingSpotToEdit: ParkingSpotEntity?
        get() = navigationArgs.editParkingSpot

    private val isEditMode: Boolean
        get() = parkingSpotToEdit != null

    private val incompleteRequest: ParkingSpotRequest?
        get() = navigationArgs.parkingSpotRequest

    private val initialDelay: Long
        get() = if (incompleteRequest == null && parkingSpotToEdit == null) 1500L else 0L

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

        navigationListener?.onSetBackArrowOnToolbar()

        setupView()

        setInitialDataWithDelay()
    }

    fun displayCarImage(uri: Uri) {
        this.carPhotoUri = uri
        binding.carPictureContainer.root.isVisible = true
        binding.carPictureContainer.vehiclePicture.setImageURI(uri)
    }

    fun displayAlarmDialog() = ensureAdded {
        dateAndTimePickers(
            true,
            { setupAlarm(this) },
            { longToast(getString(R.string.alarm_not_set)) }
        )
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
        mGoogleMap.clear()
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
                    .map { getString(it.colorNameId) }
                    .sorted()
                    .buildArrayAdapter(requireContext())
            )
    }

    private fun populateParkingFloor() {
        (binding.locationLotInfoContainer.floorTypeContainer.editText as? AutoCompleteTextView?)
            ?.setAdapter(
                ParkingFloorType
                    .values()
                    .map { getString(it.typeId) }
                    .sorted()
                    .buildArrayAdapter(requireContext())
            )
    }

    private fun populateParkingTypes() {
        val parkingTypes: List<String> = values().map { getString(it.typeId) }.sorted()
        val adapter = parkingTypes.buildArrayAdapter(requireContext())
        (binding.locationInfoContainer.parkingTypeContainer.editText as? AutoCompleteTextView?)?.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                binding.locationLotInfoContainer.root.isVisible =
                    parkingTypes[position] == getString(ParkingLot.typeId)
            }
        }
    }

    private fun fillIncompleteRequestData(request: ParkingSpotRequest) = with(binding) {
        updateParkingTime(request.parkingTime)
        locationInfoContainer.apply {
            locationAddressValue.setText(request.address)
            locationLatitudeValue.setText(request.latitude.toString())
            locationLongitudeValue.setText(request.longitude.toString())
            parkingTimeValue.setText(request.parkingTimeFormatted)
            parkingTypeValue.setText(request.parkingType)
            request.alarmTime?.let { setupAlarm(it) }
        }
        locationLotInfoContainer.apply {
            root.isVisible = ParkingType.forValue(request.parkingType) == ParkingLot
            hourRateValue.setText(request.hourRate.toString())
        }
        displayMarkerOnMap(LatLng(request.latitude, request.longitude))
    }

    private fun fillParkingSpotToEdit(spot: ParkingSpotEntity) = with(binding) {
        updateParkingTime(spot.parkingTime)
        locationInfoContainer.apply {
            locationAddressValue.setText(spot.address)
            locationLatitudeValue.setText(spot.latitude.toString())
            locationLongitudeValue.setText(spot.longitude.toString())
            parkingTimeValue.setText(spot.parkingTime.formatWith(FormatType.MONTH_DAY_YEAR_HOUR))
            parkingTypeValue.setText(getString(spot.parkingType.typeId))
            spot.alarmTime?.let { setupAlarm(it) }
        }
        locationLotInfoContainer.apply {
            root.isVisible = spot.parkingType == ParkingLot
            spot.floorType?.let { floorTypeValue.setText(getString(it.typeId)) }
            floorNumberValue.setText(spot.floorNumber)
            val colorName: String = spot.color?.colorNameId?.let { getString(it) } ?: ""
            colorValue.setText(colorName)
            lotIndentifierValue.setText(spot.lotIdentifier)
            if (spot.hourRate >= 0.0) hourRateValue.setText(spot.hourRate.toString())
        }
        spot.photo?.let { displayCarImage(it) }
        displayMarkerOnMap(spot.latLng)
    }

    private fun setupAlarm(alarmTime: DateTime) {
        this.alarmTime = alarmTime
        binding.locationInfoContainer.apply {
            parkingAlarmContainer.isVisible = true
            parkingAlarmValue.setText(alarmTime.formatWith(FormatType.MONTH_DAY_YEAR_HOUR))
        }
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
        childFragmentManager.findScrollingMapById(R.id.add_parking_map_view)
            .disableScrolling(binding.contentScroll)
            .getMapAsync {
                mGoogleMap = it
                it.mapType = GoogleMap.MAP_TYPE_HYBRID
                when {
                    incompleteRequest != null -> fillIncompleteRequestData(incompleteRequest!!)
                    isEditMode -> fillParkingSpotToEdit(parkingSpotToEdit!!)
                    else -> onGetCurrentLocation()
                }
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
        binding.locationInfoContainer.apply {
            locationAddressContainer.isErrorEnabled = false
            locationLatitudeContainer.isErrorEnabled = false
            locationLongitudeContainer.isErrorEnabled = false
            parkingTimeContainer.isErrorEnabled = false
            parkingTypeContainer.isErrorEnabled = false
        }
        binding.locationLotInfoContainer.apply {
            colorContainer.isErrorEnabled = false
            floorNumberContainer.isErrorEnabled = false
            hourRateContainer.isErrorEnabled = false
            floorTypeContainer.isErrorEnabled = false
            lotIdentifierContainer.isErrorEnabled = false
        }
    }
}
