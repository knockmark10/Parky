package com.markoid.parky.home.presentation.callbacks

interface HomeNavigationCallbacks {
    fun onUpdateToolbarMenuItems()
    fun onUpdateDrawerMenuItemVisibility(itemId: Int, isVisible: Boolean)
}
