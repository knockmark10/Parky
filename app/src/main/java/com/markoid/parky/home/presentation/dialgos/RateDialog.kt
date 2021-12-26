package com.markoid.parky.home.presentation.dialgos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.dialogs.AbstractDialog
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.appAlert
import com.markoid.parky.core.presentation.extensions.findMapById
import com.markoid.parky.core.presentation.extensions.subscribe
import com.markoid.parky.databinding.DialogRateBinding
import com.markoid.parky.home.domain.usecases.response.HourRateResponse
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel
import com.markoid.parky.position.presentation.extensions.setCameraPosition
import com.markoid.parky.position.presentation.extensions.setMarker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateDialog : AbstractDialog<DialogRateBinding>() {

    private val homeViewModel by viewModels<HomeViewModel>()

    private var mGoogleMap: GoogleMap? = null

    override fun getStyle(): Int = R.style.FullScreenDialog

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogRateBinding = DialogRateBinding.inflate(inflater, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {

        setupMap()

        setClickListener()
    }

    private fun setupMap() =
        childFragmentManager.findMapById(R.id.rate_map_view)?.getMapAsync { handleMap(it) }

    private fun handleMap(map: GoogleMap) {
        this.mGoogleMap = map
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        getHourRateData()
    }

    private fun setClickListener() {
        binding.actionAccept.setOnClickListener { dismiss() }
    }

    private fun getHourRateData() {
        homeViewModel.getHourRateData().getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> setData(it.data)
                is DataState.Error -> appAlert {
                    type = AlertType.Error
                    message = it.error
                }
            }
        }
    }

    private fun setData(data: HourRateResponse) {
        binding.address.text = data.address
        binding.hourRate.text = data.hourRate
        binding.parkingPlace.text = data.parkingLotIdentifier
        binding.parkingFloor.text = data.floor
        binding.parkingTime.text = data.parkedDuration
        binding.total.text = data.total
        mGoogleMap?.setCameraPosition(data.parkingLocation, 18f)
        mGoogleMap?.setMarker(requireContext(), data.parkingLocation, R.drawable.ic_parking_marker)
    }
}
