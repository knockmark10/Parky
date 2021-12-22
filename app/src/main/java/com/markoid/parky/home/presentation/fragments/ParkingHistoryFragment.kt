package com.markoid.parky.home.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.appAlert
import com.markoid.parky.core.presentation.extensions.subscribe
import com.markoid.parky.databinding.FragmentParkingHistoryBinding
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.presentation.adapters.ParkingHistoryAdapter
import com.markoid.parky.home.presentation.callbacks.HomeNavigationCallbacks
import com.markoid.parky.home.presentation.callbacks.ParkingHistoryAdapterCallback
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ParkingHistoryFragment :
    AbstractFragment<FragmentParkingHistoryBinding>(),
    ParkingHistoryAdapterCallback {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    private val navController by lazy { findNavController() }

    private val historyAdapter by lazy { ParkingHistoryAdapter() }

    private val homeViewModel by viewModels<HomeViewModel>()

    private var navigationListener: HomeNavigationCallbacks? = null

    private val parkingSpotId: Long
        get() = historyAdapter.getActiveSpot()?.id ?: 0L

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentParkingHistoryBinding = FragmentParkingHistoryBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        refreshToolbarItems()

        setAddParkingSpotClickListener()

        setupHistoryAdapter()

        getHistoryList()
    }

    private fun refreshToolbarItems() {
        this.navigationListener?.onUpdateToolbarMenuItems()
    }

    private fun setupHistoryAdapter() {
        historyAdapter.setAdapterListener(this)
        binding.historyList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.historyList.adapter = historyAdapter
    }

    private fun getHistoryList() {
        homeViewModel.getParkingHistory().getResult().subscribe(viewLifecycleOwner) {
            when (it) {
                is DataState.Data -> displayHistoryList(it.data)
                is DataState.Error -> showError(it.error)
            }
        }
    }

    private fun showError(message: String) {
        appAlert {
            this.message = message
            type = AlertType.Error
        }
        displayEmptyState()
    }

    private fun displayHistoryList(historyList: List<ParkingSpotEntity>) {
        if (historyList.isNotEmpty()) {
            historyAdapter.setParkingSpotHistory(historyList)
            hideEmptyState()
        } else {
            historyAdapter.flush()
            onDisplayEmptyState()
        }
    }

    private fun setAddParkingSpotClickListener() {
        binding.actionAddParkingSpot.setOnClickListener {
            binding.parkingHistoryEmptyStateContainer.parkingHistoryEmptyAnimation.cancelAnimation()
            navController.navigate(
                ParkingHistoryFragmentDirections.actionToAddParking()
            )
        }
    }

    private fun displayEmptyState() {
        binding.parkingHistoryEmptyStateContainer.root.isVisible = true
        binding.parkingHistoryEmptyStateContainer.parkingHistoryEmptyAnimation.playAnimation()
        binding.actionAddParkingSpot.isVisible = true
    }

    private fun hideEmptyState() {
        binding.parkingHistoryEmptyStateContainer.root.isVisible = false
        binding.parkingHistoryEmptyStateContainer.parkingHistoryEmptyAnimation.cancelAnimation()
        binding.actionAddParkingSpot.isVisible = false
    }

    override fun onRequestDeleteParkingSpot(spot: ParkingSpotEntity) {
        appAlert {
            message = getString(R.string.parking_spot_delete_title)
            type = AlertType.Warning
            positiveListener = {
                devicePreferences.isParkingSpotActive = false
                homeViewModel.deleteParkingSpot(spot.id)
                historyAdapter.deleteParkingSpot(spot)
                close()
            }
            negativeListener = { close() }
        }
    }

    override fun onGoToUserLocation() {
        findNavController()
            .navigate(
                ParkingHistoryFragmentDirections
                    .actionToUserLocation()
                    .setSpotId(parkingSpotId)
            )
    }

    override fun onDisplayEmptyState() {
        navigationListener?.onUpdateDrawerMenuItemVisibility(R.id.home_add_parking, true)
        navigationListener?.onUpdateDrawerMenuItemVisibility(R.id.home_user_location, false)
        displayEmptyState()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeNavigationCallbacks) {
            this.navigationListener = context
        }
    }

    companion object {
        const val SPOT_ID = "spot.id"
    }
}
