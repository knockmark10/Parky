package com.markoid.parky.core.presentation.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

@Suppress("UNCHECKED_CAST")
fun <F : Fragment> AppCompatActivity.findFragmentByClassName(fragmentClass: Class<F>): F? {
    val navHostFragment = supportFragmentManager.primaryNavigationFragment as? NavHostFragment
    val desiredFragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment as? F
    if (desiredFragment != null) {
        return desiredFragment
    } else {
        navHostFragment?.childFragmentManager?.fragments?.forEach {
            if (fragmentClass.isAssignableFrom(it.javaClass)) {
                return it as? F
            }
        }
    }
    return null
}
