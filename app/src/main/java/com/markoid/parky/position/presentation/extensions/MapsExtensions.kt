package com.markoid.parky.position.presentation.extensions

import android.content.res.Resources
import android.graphics.Color
import androidx.annotation.ColorInt
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

fun GoogleMap.drawCircle(
    location: LatLng,
    radius: Double,
    @ColorInt strokeColor: Int = Color.BLACK,
    @ColorInt fillColor: Int = 0x30ff0000,
    strokeWidth: Float = 2f
): Circle {
    val circleOptions = CircleOptions().apply {
        center(location)
        radius(radius)
        strokeColor(strokeColor)
        fillColor(fillColor)
        strokeWidth(strokeWidth)
    }

    return this.addCircle(circleOptions)
}

fun GoogleMap.centerMarkers(resources: Resources, latLngList: List<LatLng>) {
    val latLngBounds = LatLngBounds.Builder()

    latLngList.forEach { latLngBounds.include(it) }

    val bounds = latLngBounds.build()
    val width = resources.displayMetrics.widthPixels
    val height = resources.displayMetrics.heightPixels
    val padding = (width * 0.10).toInt()

    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
    this.animateCamera(cameraUpdate)
}

fun GoogleMap.setCameraPosition(position: LatLng, zoom: Float) {
    val cameraPosition = CameraPosition.Builder().target(position).zoom(zoom).build()
    this.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

fun GoogleMap.centerAddedMarkers(listMarker: List<Marker>, zoom: Float) {
    var latitude = 0.0
    var longitude = 0.0
    for (marker in listMarker) {
        latitude += marker.position.latitude
        longitude += marker.position.longitude
    }

    if (listMarker.isNotEmpty()) {
        latitude /= listMarker.size
        longitude /= listMarker.size
    }

    val cameraPosition =
        CameraPosition.Builder().target(LatLng(latitude, longitude)).zoom(zoom).build()
    this.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}