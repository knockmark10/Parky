package com.markoid.parky.home.presentation.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.markoid.parky.R
import com.markoid.parky.core.presentation.activities.AbstractActivity
import com.markoid.parky.core.presentation.extensions.findFragmentByClassName
import com.markoid.parky.databinding.ActivityHomeBinding
import com.markoid.parky.home.presentation.callbacks.HomeNavigationCallbacks
import com.markoid.parky.home.presentation.fragments.AddParkingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AbstractActivity<ActivityHomeBinding>(), HomeNavigationCallbacks {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val navController by lazy { findNavController(R.id.home_navigation_host) }

    private val addParkingFragment by lazy { findFragmentByClassName(AddParkingFragment::class.java) }

    private val currentDestination: String
        get() = navController.currentDestination?.label?.toString() ?: getString(R.string.menu_home)

    private val toolbarMenu: Int
        get() = when (currentDestination) {
            getString(R.string.menu_add_parking) -> R.menu.add_parking_menu
            else -> R.menu.home_menu
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(toolbarMenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_take_photo -> addParkingFragment?.takePhoto()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.homeLayout.homeToolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.homeNavigationView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_parking_history,
                R.id.home_add_parking,
                R.id.nav_slideshow,
                R.id.home_settings
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        invalidateOptionsMenu()
    }

    override fun onNavigationChanged() {
        invalidateOptionsMenu()
    }
}
