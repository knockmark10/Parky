package com.markoid.parky.home.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.markoid.parky.R
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.core.presentation.extensions.findMapById
import com.markoid.parky.databinding.FragmentHomeBinding
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.presentation.extensions.setCameraPosition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : AbstractFragment<FragmentHomeBinding>() {

    private lateinit var mGoogleMap: GoogleMap

    private val homeViewModel: HomeViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {

        setupMap()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findMapById(R.id.home_map)
        mapFragment?.getMapAsync { handleMap(it) }
    }

    private fun handleMap(map: GoogleMap) {
        this.mGoogleMap = map
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        getLocation()
    }

    /**
     * When this method gets called, the map is already loaded, so all map operations going on
     * are safe to execute.
     */
    private fun getLocation() {
        homeViewModel.getCurrentLocation().getResult().observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Data -> onLocationReceived(it.data)
                is DataState.Error -> Log.e("ERROR", it.error)
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun onLocationReceived(position: PositionEntity) {
        this.mGoogleMap.setCameraPosition(position.latLng, 18f)
        this.mGoogleMap.isMyLocationEnabled = true
    }
}
