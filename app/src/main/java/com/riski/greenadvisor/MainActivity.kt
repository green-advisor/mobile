package com.riski.greenadvisor

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import com.riski.greenadvisor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    companion object {
        const val HOME_ITEM = R.id.navigation_home
        const val CAMERA_ITEM = R.id.navigation_camera
        const val MAP_ITEM = R.id.navigation_map
        const val SHOP_ITEM = R.id.navigation_shop
        const val USER_ITEM = R.id.navigation_user
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavHost()
        setUpBottomNavigation()
    }

    private fun initNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setUpBottomNavigation() {
        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(HOME_ITEM, getString(R.string.home), R.drawable.ic_home),
            CurvedBottomNavigation.Model(CAMERA_ITEM, getString(R.string.camera), R.drawable.ic_filter_center_focus),
            CurvedBottomNavigation.Model(MAP_ITEM, getString(R.string.maps), R.drawable.ic_home_pin),
            CurvedBottomNavigation.Model(SHOP_ITEM, getString(R.string.shop), R.drawable.ic_shopping_bag),
            CurvedBottomNavigation.Model(USER_ITEM, getString(R.string.user), R.drawable.ic_account_circle),
        )
        binding.navView.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener {
                navController.navigate(it.id)
            }
            show(HOME_ITEM)
            setupNavController(navController)
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (navController.currentDestination!!.id == HOME_ITEM)
            super.onBackPressed()
        else {
            when (navController.currentDestination!!.id) {
                CAMERA_ITEM -> {
                    navController.popBackStack(R.id.navigation_home, false)
                }
                MAP_ITEM -> {
                    navController.popBackStack(R.id.navigation_home, false)
                }
                SHOP_ITEM -> {
                    navController.popBackStack(R.id.navigation_home, false)
                }
                USER_ITEM -> {
                    navController.popBackStack(R.id.navigation_home, false)
                }
                else -> {
                    navController.navigateUp()
                }
            }
        }
    }
}