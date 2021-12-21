package com.markoid.parky.position.presentation.managers

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.* // ktlint-disable no-wildcard-imports
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.joda.time.Duration
import java.util.* // ktlint-disable no-wildcard-imports
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@SuppressLint("MissingPermission")
class PositionManager(
    private val mContext: Context,
    private val mLooper: Looper
) {

    private val mGeocoder: Geocoder by lazy { Geocoder(mContext, Locale.getDefault()) }

    private var mFusedClient: FusedLocationProviderClient? = null

    private var mLocationCallback: LocationCallback? = null

    /**
     * This request will be used for a one-time request
     */
    private val smartLocationRequest: LocationRequest by lazy {
        LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = Duration.standardSeconds(15L).millis
            fastestInterval = Duration.standardSeconds(10L).millis
        }
    }

    init {
        installLocationSettings()
    }

    /**
     * Request and receive location once.
     */
    suspend fun requestSingleLocation(): Location? = suspendCancellableCoroutine { task ->
        this.mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                // Ignore any null response
                val location = p0?.lastLocation ?: return
                stopLocationUpdates()
                task.resume(location)
            }
        }
        this.mFusedClient
            ?.requestLocationUpdates(this.smartLocationRequest, mLocationCallback, this.mLooper)
            ?.addOnFailureListener { task.resumeWithException(it) }
    }

    /**
     * Request location updates. This will emit a new location every given interval.
     */
    fun observeLocationUpdates(): Flow<Location> = callbackFlow {
        Log.d("PositionManager", "observing location updates")
        val client = LocationServices.getFusedLocationProviderClient(mContext)
        val locationRequest = LocationRequest
            .create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(Duration.standardSeconds(0).millis)
            .setFastestInterval(Duration.standardSeconds(0).millis)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    Log.d("PositionManager", "got location ${locationResult.lastLocation}")
                    trySend(locationResult.lastLocation)
                }
            }
        }

        client.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            Log.d("PositionManager", "stop observing location updates")
            client.removeLocationUpdates(locationCallback)
        }
    }

    private fun installLocationSettings() {
        LocationServices
            .getSettingsClient(this.mContext)
            .checkLocationSettings(
                LocationSettingsRequest
                    .Builder()
                    .apply { addLocationRequest(smartLocationRequest) }
                    .build()
            )
        this.mFusedClient = LocationServices.getFusedLocationProviderClient(this.mContext)
    }

    suspend fun getLastLocation(): Location = suspendCoroutine { task ->
        this.mFusedClient?.lastLocation
            ?.addOnSuccessListener { task.resume(it) }
            ?.addOnFailureListener { task.resumeWithException(it) }
    }

    fun getAddressFromLocation(location: LatLng, maxResults: Int = 1): List<Address>? =
        this.mGeocoder.getFromLocation(location.latitude, location.longitude, maxResults)

    fun getAddressFromLocationName(locationName: String, maxResults: Int = 1): List<Address>? =
        this.mGeocoder.getFromLocationName(locationName, maxResults)

    /**
     * Stop receiving location updates (turn off gps use)
     */
    fun stopLocationUpdates() {
        if (mLocationCallback != null) {
            this.mFusedClient?.removeLocationUpdates(mLocationCallback)
        }
    }

    companion object {
        /**
         * Returns the straight-line distance in meters between two locations
         */
        fun getDistanceFromLocations(firstLocation: LatLng, secondLocation: LatLng): Double {
            val results = FloatArray(1)
            Location.distanceBetween(
                firstLocation.latitude,
                firstLocation.longitude,
                secondLocation.latitude,
                secondLocation.longitude,
                results
            )
            return results.firstOrNull()?.toDouble() ?: 0.0
        }
    }
}
