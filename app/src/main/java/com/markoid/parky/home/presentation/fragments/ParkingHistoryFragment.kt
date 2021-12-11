package com.markoid.parky.home.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.databinding.FragmentParkingHistoryBinding

class ParkingHistoryFragment : AbstractFragment<FragmentParkingHistoryBinding>() {

    private val navController by lazy { findNavController() }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentParkingHistoryBinding = FragmentParkingHistoryBinding
        .inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {
        binding.parkingHistoryAddParkingBtn.setOnClickListener {
            navController.navigate(
                ParkingHistoryFragmentDirections
                    .actionHomeParkingHistoryToHomeAddParking()
            )
        }
    }
}