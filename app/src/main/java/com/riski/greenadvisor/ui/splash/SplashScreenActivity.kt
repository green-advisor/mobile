@file:Suppress("DEPRECATION")

package com.riski.greenadvisor.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.riski.greenadvisor.MainActivity
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.databinding.ActivitySplashScreenBinding
import com.riski.greenadvisor.ui.greetings.GreetingsActivity
import com.riski.greenadvisor.ui.home.HomeViewModel
import com.riski.greenadvisor.ui.home.HomeViewModelFactory
import com.riski.greenadvisor.ui.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val splashDuration = 2000L
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var splashScreenViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        animationSet()

    }

    override fun onResume() {
        super.onResume()
        checkFirstRun()
    }

    private fun checkFirstRun() {
        val isFirstRun = readFirstRunStatus()
        if (isFirstRun) {
            saveFirstRunStatus(false)
            startActivity(Intent(this, GreetingsActivity::class.java))
            finish()
        } else {
            countinueToNextActivity()
        }
    }

    private fun readFirstRunStatus(): Boolean {
        val dataStoreKey = (booleanPreferencesKey ("FIRST_RUN_STATUS"))
        val dataStore = dataStore
        val isFirstRun = runBlocking {
            dataStore.data.map { preference ->
                preference[dataStoreKey] ?: true
            }.first()
        }
        return isFirstRun
    }

    private fun saveFirstRunStatus(isFirstRun: Boolean) {
        val dataStoreKey = booleanPreferencesKey("FIRST_RUN_STATUS")
        val dataStore = dataStore
        runBlocking {
            dataStore.edit { preferences ->
                preferences[dataStoreKey] = isFirstRun
            }
        }
    }

    private fun countinueToNextActivity() {
        splashScreenViewModel = ViewModelProvider(this, HomeViewModelFactory(Preference.getInstance(dataStore), this))[HomeViewModel::class.java]
        splashScreenViewModel.getUser().observe(this) { user ->
            val targetClass = if (user.isLogin) {
                MainActivity::class.java
            } else {
                LoginActivity::class.java
            }
            val intent = Intent(this, targetClass)
            startActivity(intent)
            finish()
        }
    }

//    private fun saveLogin() {
//        splashScreenViewModel = ViewModelProvider(this, HomeViewModelFactory(Preference.getInstance(dataStore), this))[HomeViewModel::class.java]
//
//        splashScreenViewModel.getUser().observe(this) {
//            if (it.isLogin) {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            } else {
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//            }
//        }
//    }

    private fun animationSet() {
        val splashImage = binding.splashImage

        val animSlideLeft = ObjectAnimator.ofFloat(splashImage, "translationX", 0f, -100f)
        animSlideLeft.duration = splashDuration / 2
        animSlideLeft.interpolator = AccelerateDecelerateInterpolator()

        val animSlideRight = ObjectAnimator.ofFloat(splashImage, "translationX", -100f, 100f)
        animSlideRight.duration = splashDuration / 2
        animSlideRight.interpolator = AccelerateDecelerateInterpolator()

        animSlideLeft.start()

        animSlideLeft.addUpdateListener {
            if (it.animatedFraction >= 1f) {
                animSlideRight.start()
            }
        }

        animSlideRight.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                countinueToNextActivity()
            }
//            override fun onAnimationEnd(animation: Animator) {
//                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@SplashScreenActivity)
//                val joinStatus = sharedPreferences.getBoolean("JOIN_STATUS", false)
//
//
//                val targetClass = if (joinStatus) {
//                    RegisterActivity::class.java
//                } else {
//                    GreetingsActivity::class.java
//                }
//                val intent = Intent(this@SplashScreenActivity, targetClass)
//                startActivity(intent)
//                finish()
//            }
        })
    }
}