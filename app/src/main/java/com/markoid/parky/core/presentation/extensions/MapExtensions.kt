package com.markoid.parky.core.presentation.extensions

import androidx.annotation.IdRes
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.SupportMapFragment
import com.markoid.parky.core.presentation.views.MapScrollingView

fun FragmentManager.findMapById(@IdRes id: Int): SupportMapFragment? =
    findFragmentById(id) as SupportMapFragment?

fun FragmentManager.findScrollingMapById(@IdRes id: Int): MapScrollingView =
    findFragmentById(id) as MapScrollingView

fun MapScrollingView.disableScrolling(nestedScrollView: NestedScrollView): MapScrollingView {
    setNewOnTouchListener { nestedScrollView.requestDisallowInterceptTouchEvent(true) }
    return this
}
