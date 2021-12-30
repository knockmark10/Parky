package com.markoid.parky.home.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.core.presentation.extensions.appAlert
import com.markoid.parky.core.presentation.extensions.subscribe
import com.markoid.parky.core.presentation.extensions.verticalLayoutManager
import com.markoid.parky.core.presentation.notifications.NotificationConstants.PARKING_SPOT_REQUEST_ARG
import com.markoid.parky.core.presentation.notifications.NotificationIntentActions
import com.markoid.parky.databinding.FragmentParkingHistoryBinding
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.isActive
import com.markoid.parky.home.data.entities.isArchived
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.presentation.adapters.ParkingHistoryAdapter
import com.markoid.parky.home.presentation.callbacks.ParkingHistoryAdapterCallback
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ParkingHistoryFragment :
    HomeBaseFragment<FragmentParkingHistoryBinding>(),
    ParkingHistoryAdapterCallback {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    private val navController by lazy { findNavController() }

    private val historyAdapter by lazy { ParkingHistoryAdapter() }

    private val parkingSpotId: Long
        get() = historyAdapter.getActiveSpot()?.id ?: 0L

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentParkingHistoryBinding = FragmentParkingHistoryBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        setAddParkingSpotClickListener()

        setupHistoryAdapter()

        getHistoryList()
    }

    private fun setupHistoryAdapter() {
        historyAdapter.setAdapterListener(this)
        binding.parkingHistoryListContainer.historyList.apply {
            layoutManager = requireContext().verticalLayoutManager
            adapter = historyAdapter
        }
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
        when {
            // There is an Active parking spot
            historyList.any { it.status.isActive } -> {
                historyAdapter.setParkingSpotHistory(historyList)
                hideEmptyState()
            }

            // There is at least an Archived spot
            historyList.any { it.status.isArchived } -> {
                historyAdapter.setParkingSpotHistory(historyList)
                hideEmptyState(true)
            }

            // There are no parking spots (neither Active nor Archived)
            else -> {
                historyAdapter.flush()
                onDisplayEmptyState()
            }
        }
        readPotentialNotificationIntent()
    }

    private fun readPotentialNotificationIntent() {
        // Save the reference for the caught intent
        val caughtIntent = requireActivity().intent
        // Recover the action that we will use to determine what to do
        val action = caughtIntent.action?.let { NotificationIntentActions.getTypeByAction(it) }
        // Clear the action from the caught intent
        caughtIntent.action = null
        // Set the activity's intent with the clear action one
        requireActivity().intent = caughtIntent
        when (action) {
            NotificationIntentActions.ACTION_ADD_PARKING -> {
                val request = requireActivity().intent
                    .getSerializableExtra(PARKING_SPOT_REQUEST_ARG) as? ParkingSpotRequest?
                goToAddParkingSpot(request)
            }
            NotificationIntentActions.ACTION_USER_LOCATION -> onGoToUserLocation()
            NotificationIntentActions.ACTION_SETTINGS -> goToSettings()
        }
    }

    private fun setAddParkingSpotClickListener() {
        binding.actionAddParkingSpot.setOnClickListener {
            binding.parkingHistoryEmptyStateContainer.parkingHistoryEmptyAnimation.cancelAnimation()
            goToAddParkingSpot()
        }
    }

    private fun displayEmptyState() {
        binding.parkingHistoryListContainer.root.isVisible = false
        binding.parkingHistoryEmptyStateContainer.root.isVisible = true
        binding.parkingHistoryEmptyStateContainer.parkingHistoryEmptyAnimation.playAnimation()
        binding.actionAddParkingSpot.isVisible = true
    }

    private fun hideEmptyState(isAddButtonVisible: Boolean = false) {
        binding.parkingHistoryListContainer.root.isVisible = true
        binding.parkingHistoryEmptyStateContainer.root.isVisible = false
        binding.parkingHistoryEmptyStateContainer.parkingHistoryEmptyAnimation.cancelAnimation()
        binding.actionAddParkingSpot.isVisible = isAddButtonVisible
    }

    private fun goToSettings() {
        navController.navigate(
            ParkingHistoryFragmentDirections.actionToSettings()
        )
    }

    private fun goToAddParkingSpot(request: ParkingSpotRequest? = null) {
        navController.navigate(
            ParkingHistoryFragmentDirections.actionToAddParking().setParkingSpotRequest(request)
        )
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

    override fun onEditParkingSpot(spot: ParkingSpotEntity) {
        navController.navigate(
            ParkingHistoryFragmentDirections.actionToAddParking().setEditParkingSpot(spot)
        )
    }

    override fun onGoToUserLocation() {
        navController.navigate(
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
}
