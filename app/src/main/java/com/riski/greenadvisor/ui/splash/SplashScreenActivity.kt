@file:Suppress("DEPRECATION")

package com.riski.greenadvisor.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.animation.AccelerateDecelerateInterpolator
import com.riski.greenadvisor.databinding.ActivitySplashScreenBinding
import com.riski.greenadvisor.ui.greetings.GreetingsActivity
import com.riski.greenadvisor.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_DURATION = 2000L
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        val splashImage = binding.splashImage

        val animSlideLeft = ObjectAnimator.ofFloat(splashImage, "translationX", 0f, -100f)
        animSlideLeft.duration = SPLASH_DURATION / 2
        animSlideLeft.interpolator = AccelerateDecelerateInterpolator()

        val animSlideRight = ObjectAnimator.ofFloat(splashImage, "translationX", -100f, 100f)
        animSlideRight.duration = SPLASH_DURATION / 2
        animSlideRight.interpolator = AccelerateDecelerateInterpolator()

        animSlideLeft.start()

        animSlideLeft.addUpdateListener {
            if (it.animatedFraction >= 1f) {
                animSlideRight.start()
            }
        }

        animSlideRight.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@SplashScreenActivity)
                val joinStatus = sharedPreferences.getBoolean("JOIN_STATUS", false)

                val targetClass = if (joinStatus) {
                    LoginActivity::class.java
                } else {
                    GreetingsActivity::class.java
                }
                val intent = Intent(this@SplashScreenActivity, targetClass)
                startActivity(intent)
                finish()
            }
        })
    }
}