package com.markoid.parky.position.presentation.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.markoid.parky.R

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

fun GoogleMap.centerWithLatLngList(resources: Resources, latLngList: List<LatLng>) {
    val latLngBounds = LatLngBounds.Builder()

    latLngList.forEach { latLngBounds.include(it) }

    val bounds = latLngBounds.build()
    val width = resources.displayMetrics.widthPixels
    val height = resources.displayMetrics.heightPixels
    val padding = (width * 0.25).toInt()

    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
    this.animateCamera(cameraUpdate)
}

fun GoogleMap.setCameraPosition(position: LatLng, zoom: Float) {
    val cameraPosition = CameraPosition.Builder().target(position).zoom(zoom).build()
    this.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

fun GoogleMap.centerWithMarkers(listMarker: List<Marker>, zoom: Float) {
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

fun Context.getBitmapFromDrawable(@DrawableRes resId: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(this, resId) ?: return null
    vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun Context.getMarkerBitmapFromView(@DrawableRes resourceId: Int): BitmapDescriptor {
    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val customMarkerView = inflater.inflate(R.layout.content_custom_marker, null)
    val markerImageView = customMarkerView.findViewById(R.id.custom_marker_icon) as ImageView

    markerImageView.setImageResource(resourceId)
    customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    customMarkerView.layout(
        0,
        0,
        customMarkerView.measuredWidth,
        customMarkerView.measuredHeight
    )

    val returnedBitmap = Bitmap.createBitmap(
        customMarkerView.measuredWidth, customMarkerView.measuredHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(returnedBitmap)
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)

    customMarkerView.background?.draw(canvas)

    customMarkerView.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(returnedBitmap)
}

fun GoogleMap.setMarker(context: Context, position: LatLng, @DrawableRes resId: Int): Marker =
    addMarker(MarkerOptions().position(position).icon(context.getMarkerBitmapFromView(resId)))

fun GoogleMap.setTheme(context: Context, style: Int): Boolean = try {
    setMapStyle(MapStyleOptions.loadRawResourceStyle(context, style))
} catch (error: Resources.NotFoundException) {
    error.printStackTrace()
    false
}

fun GoogleMap.setDarkMode(context: Context) {
    val themeApplied = setTheme(context, R.raw.night_map)
    if (themeApplied.not()) Log.e("GOOGLEMAP", "Night mode not applied to map.")
}
