package com.markoid.parky.home.presentation.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavOptions
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
import com.markoid.parky.home.presentation.fragments.UserLocationFragment
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AbstractActivity<ActivityHomeBinding>(), HomeNavigationCallbacks {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val navController by lazy { findNavController(R.id.home_navigation_host) }

    private val addParkingFragment by lazy { findFragmentByClassName(AddParkingFragment::class.java) }

    private val userLocationFragment by lazy { findFragmentByClassName(UserLocationFragment::class.java) }

    private val currentDestination: String
        get() = navController.currentDestination?.label?.toString() ?: getString(R.string.menu_home)

    private val toolbarMenu: Int
        get() = when (currentDestination) {
            getString(R.string.menu_add_parking) -> R.menu.add_parking_menu
            getString(R.string.menu_user_location) -> R.menu.user_location_menu
            else -> -1
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (toolbarMenu != -1) menuInflater.inflate(toolbarMenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_take_photo -> addParkingFragment?.onTakePhoto()
            R.id.action_schedule_alarm -> addParkingFragment?.displayAlarmDialog()
            R.id.action_refresh_location -> addParkingFragment?.onGetCurrentLocation()
            R.id.action_change_map -> userLocationFragment?.displayMapTypeDialog()
            R.id.action_finish_parking -> userLocationFragment?.finishParking()
            android.R.id.home -> if (currentDestination != getString(R.string.menu_home)) {
                navController.popBackStack()
                // This will prevent the drawer to open up
                return true
            }
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
                R.id.home_user_location,
                R.id.home_settings
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        checkMenuItemsVisibility()
        setDrawerNavigationTransitions()
    }

    private fun checkMenuItemsVisibility() {
        onUpdateDrawerMenuItemVisibility(
            R.id.home_add_parking,
            devicePreferences.isParkingSpotActive.not()
        )
        onUpdateDrawerMenuItemVisibility(
            R.id.home_user_location,
            devicePreferences.isParkingSpotActive
        )
    }

    private fun setDrawerNavigationTransitions() {
        val optionsBuilder = NavOptions.Builder()
        binding.homeNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_add_parking,
                R.id.home_user_location,
                R.id.home_settings ->
                    optionsBuilder
                        .setEnterAnim(R.anim.left_in)
                        .setExitAnim(R.anim.left_out)
                        .setPopEnterAnim(R.anim.right_in)
                        .setPopExitAnim(R.anim.right_out)
            }
            navController.navigate(it.itemId, null, optionsBuilder.build())
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        invalidateOptionsMenu()
    }

    override fun onUpdateToolbarMenuItems() {
        invalidateOptionsMenu()
    }

    override fun onUpdateDrawerMenuItemVisibility(itemId: Int, isVisible: Boolean) {
        val navigationView: NavigationView? = findViewById(R.id.home_navigation_view)
        navigationView?.menu?.findItem(itemId)?.isVisible = isVisible
    }

    override fun onSetBackArrowOnToolbar() {
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_back)
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
