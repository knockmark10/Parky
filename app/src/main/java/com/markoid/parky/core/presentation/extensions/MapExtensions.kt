package com.markoid.parky.core.presentation.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.SupportMapFragment

fun FragmentManager.findMapById(@IdRes id: Int): SupportMapFragment? =
    findFragmentById(id) as SupportMapFragment?
