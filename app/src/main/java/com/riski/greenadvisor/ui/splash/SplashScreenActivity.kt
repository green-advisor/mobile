@file:Suppress("DEPRECATION")

package com.riski.greenadvisor.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.riski.greenadvisor.MainActivity
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.databinding.ActivitySplashScreenBinding
import com.riski.greenadvisor.ui.greetings.GreetingsActivity
import com.riski.greenadvisor.ui.home.HomeViewModel
import com.riski.greenadvisor.ui.home.HomeViewModelFactory
import com.riski.greenadvisor.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val splashDuration = 1000L
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var splashScreenViewModel: HomeViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            checkGreetingsActivity()
        }, splashDuration)
    }

    private fun checkGreetingsActivity() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val greetingsShown = sharedPreferences.getBoolean("GREETINGS_SHOWN", false)

        if (greetingsShown) {
            saveLogin()
        } else {
            val editor = sharedPreferences.edit()
            editor.putBoolean("GREETINGS_SHOWN", true)
            editor.apply()
            startActivity(Intent(this, GreetingsActivity::class.java))
            finish()
        }
    }

    private fun saveLogin() {
        splashScreenViewModel = ViewModelProvider(this, HomeViewModelFactory(Preference.getInstance(dataStore), this))[HomeViewModel::class.java]

        splashScreenViewModel.getUser().observe(this) {
            if (it.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }
    }
}