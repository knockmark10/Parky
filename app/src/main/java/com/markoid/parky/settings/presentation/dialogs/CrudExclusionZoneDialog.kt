package com.markoid.parky.settings.presentation.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.R
import com.markoid.parky.core.presentation.dialogs.AbstractDialog
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.appAlert
import com.markoid.parky.core.presentation.extensions.findMapById
import com.markoid.parky.core.presentation.extensions.value
import com.markoid.parky.core.presentation.views.buildArrayAdapter
import com.markoid.parky.databinding.DialogAddExclusionZoneBinding
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.home.presentation.enums.ParkingColor
import com.markoid.parky.position.presentation.extensions.drawCircle
import com.markoid.parky.position.presentation.extensions.setCameraPosition
import com.markoid.parky.position.presentation.extensions.setDarkMode
import com.markoid.parky.settings.domain.requests.ExclusionZoneRequest
import com.markoid.parky.settings.domain.responses.ExclusionZoneValidationStatus
import com.markoid.parky.settings.presentation.dialogs.AddExclusionZoneDialog.Companion.EXCLUSION_ZONE_ARG

abstract class CrudExclusionZoneDialog : AbstractDialog<DialogAddExclusionZoneBinding>() {

    abstract val isDarkMode: Boolean

    abstract fun onGetRealTimeLocationUpdates()

    abstract fun onUpdateExclusionZone(zoneToUpdate: ExclusionZoneRequest)

    abstract fun onSaveNewExclusionZone(zone: ExclusionZoneRequest)

    abstract fun onRefreshAdapter()

    private var userLocation: LatLng = LatLng(0.0, 0.0)

    private var isCameraSet: Boolean = false

    private lateinit var mGoogleMap: GoogleMap

    private var exclusionZoneCircle: Circle? = null

    private val zoneToEdit: ExclusionZoneEntity?
        get() = arguments?.getSerializable(EXCLUSION_ZONE_ARG) as? ExclusionZoneEntity?

    private val isEditMode: Boolean
        get() = zoneToEdit != null

    private val currentExclusionZone: ExclusionZoneRequest
        get() = ExclusionZoneRequest(
            color = binding.colorValue.value,
            latitude = zoneToEdit?.latitude ?: userLocation.latitude,
            longitude = zoneToEdit?.longitude ?: userLocation.longitude,
            id = zoneToEdit?.id,
            name = binding.exclusionZoneNameValue.value,
            radius = binding.radiusSlider.value.toDouble()
        )

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogAddExclusionZoneBinding = DialogAddExclusionZoneBinding
        .inflate(inflater, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {

        populateColors()

        setupMap()
    }

    private fun setupMap() =
        childFragmentManager.findMapById(R.id.exclusion_map)?.getMapAsync { onMapReady(it) }

    @SuppressLint("MissingPermission")
    private fun onMapReady(map: GoogleMap) {
        this.mGoogleMap = map
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (isDarkMode) map.setDarkMode(requireContext())
        map.isMyLocationEnabled = isEditMode.not()
        setupView()
    }

    private fun setupView() {
        setClickListeners()
        setupRadiusSlider()
        if (isEditMode) setEditingInformation()
        else onGetRealTimeLocationUpdates()
    }

    private fun setClickListeners() = with(binding) {
        actionSave.setOnClickListener {
            if (isEditMode) onUpdateExclusionZone(currentExclusionZone)
            else onSaveNewExclusionZone(currentExclusionZone)
        }
        actionCancel.setOnClickListener { dismiss() }
    }

    private fun setupRadiusSlider() = with(binding) {
        radiusSlider.isEnabled = true
        radiusSlider.addOnChangeListener { _, value, _ ->
            radiusLabel.text = String.format("Radius: %d m", value.toInt())
            updateCircleRadius(
                radius = value.toDouble(),
                circleLocation = zoneToEdit?.location ?: userLocation
            )
        }
    }

    private fun setEditingInformation() = this.zoneToEdit?.let {
        with(binding) {
            exclusionZoneNameValue.setText(it.name)
            colorValue.setText(getString(it.color.colorNameId))
            radiusSlider.value = it.radius.toFloat()
            updateCircleRadius(it.radius, it.location)
            centerCameraOnLocation(it.location)
        }
    }

    private fun updateCircleRadius(radius: Double, circleLocation: LatLng) {
        this.exclusionZoneCircle?.remove()
        this.exclusionZoneCircle = this.mGoogleMap.drawCircle(
            location = circleLocation,
            radius = radius,
            fillColor = getSelectedColor()
        )
    }

    private fun populateColors() {
        (binding.colorContainer.editText as? AutoCompleteTextView?)
            ?.setAdapter(
                ParkingColor
                    .values()
                    .map { getString(it.colorNameId) }
                    .sorted()
                    .buildArrayAdapter(requireContext())
            )
    }

    private fun getSelectedColor(): Int {
        val colorValue = binding.colorValue.text.toString()
        val parkingColor = ParkingColor.forValue(colorValue, resources)
        return parkingColor?.let { getResolvedColor(it.transparentColorId) }
            ?: getResolvedColor(R.color.md_blue_500_50)
    }

    private fun displaySuccess() {
        appAlert {
            type = AlertType.Success
            message = getString(R.string.exclusion_zone_saved_successfully)
            positiveListener = {
                onRefreshAdapter()
                close()
                dismiss()
            }
        }
    }

    fun handleResults(status: ExclusionZoneValidationStatus) {
        when (status) {
            is ExclusionZoneValidationStatus.Failure.InvalidLocation ->
                showError(getString(status.resId))
            is ExclusionZoneValidationStatus.Failure.MissingName ->
                binding.exclusionZoneNameContainer.error = getString(status.resId)
            is ExclusionZoneValidationStatus.Failure.WrongColor ->
                binding.colorContainer.error = getString(status.resId)
            ExclusionZoneValidationStatus.Success -> displaySuccess()
        }
    }

    fun showError(error: String) {
        appAlert {
            type = AlertType.Error
            message = error
        }
    }

    fun updateUserLocation(location: LatLng) {
        this.userLocation = location
    }

    fun centerCameraOnLocation(location: LatLng) {
        if (isCameraSet.not()) {
            isCameraSet = true
            mGoogleMap.setCameraPosition(location, 15f)
        }
    }
}
