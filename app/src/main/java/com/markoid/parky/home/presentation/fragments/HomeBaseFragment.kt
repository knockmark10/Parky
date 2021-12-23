package com.markoid.parky.home.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.home.presentation.callbacks.HomeNavigationCallbacks
import com.markoid.parky.home.presentation.viewmodels.HomeViewModel

abstract class HomeBaseFragment<T : ViewBinding> : AbstractFragment<T>() {

    var navigationListener: HomeNavigationCallbacks? = null

    val homeViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.startObservingLifecycle(lifecycle)
        navigationListener?.onUpdateToolbarMenuItems()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeNavigationCallbacks) navigationListener = context
    }
}
