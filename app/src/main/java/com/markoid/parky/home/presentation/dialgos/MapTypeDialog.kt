package com.markoid.parky.home.presentation.dialgos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.markoid.parky.R
import com.markoid.parky.core.presentation.dialogs.AbstractDialog
import com.markoid.parky.databinding.DialogMapTypeBinding
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

typealias OnMapSelectedListener = (Int) -> Unit

@AndroidEntryPoint
class MapTypeDialog : AbstractDialog<DialogMapTypeBinding>() {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    private var mListener: OnMapSelectedListener? = null

    private var selectedType: Int = GoogleMap.MAP_TYPE_HYBRID

    override fun getStyle(): Int = R.style.FullWidthDialog

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogMapTypeBinding = DialogMapTypeBinding
        .inflate(inflater, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {

        initRadioButtonSelection()

        setRadioButtonSelectedListener()

        setButtonsClickListener()
    }

    fun setOnMapTypeSelectedListener(listener: OnMapSelectedListener): MapTypeDialog {
        this.mListener = listener
        return this
    }

    private fun initRadioButtonSelection() {
        when (val type = devicePreferences.mapType) {
            GoogleMap.MAP_TYPE_NONE -> binding.none
            GoogleMap.MAP_TYPE_NORMAL -> binding.normal
            GoogleMap.MAP_TYPE_SATELLITE -> binding.satellite
            GoogleMap.MAP_TYPE_TERRAIN -> binding.terrain
            GoogleMap.MAP_TYPE_HYBRID -> binding.hybrid
            else -> throw IllegalStateException("Unknown type $type.")
        }.isChecked = true
    }

    private fun setRadioButtonSelectedListener() {
        binding.mapTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedType = when (checkedId) {
                R.id.none -> GoogleMap.MAP_TYPE_NONE
                R.id.normal -> GoogleMap.MAP_TYPE_NORMAL
                R.id.satellite -> GoogleMap.MAP_TYPE_SATELLITE
                R.id.terrain -> GoogleMap.MAP_TYPE_TERRAIN
                R.id.hybrid -> GoogleMap.MAP_TYPE_HYBRID
                else -> throw IllegalStateException("Unknown id $checkedId.")
            }
        }
    }

    private fun setButtonsClickListener() {
        binding.actionAccept.setOnClickListener { onAccept() }
        binding.actionCancel.setOnClickListener { dismiss() }
    }

    private fun onAccept() {
        devicePreferences.mapType = selectedType
        mListener?.invoke(selectedType)
        dismiss()
    }
}
