package com.riski.greenadvisor

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.riski.greenadvisor.databinding.ActivityBetaBinding

class BetaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBetaBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBetaBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.beta)
            setDisplayHomeAsUpEnabled(true)
        }

        setImageIntent()
        setImageAnimation()

    }

    private fun setImageIntent() {
        binding.betaImage.setOnClickListener {
            Toast.makeText(this, getString(R.string.beta_test), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setImageAnimation() {
        val rotateAnimation = RotateAnimation(0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.interpolator = LinearInterpolator()
        rotateAnimation.duration = DURATION
        rotateAnimation.repeatCount = Animation.INFINITE
        binding.betaImage.startAnimation(rotateAnimation)
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        private const val DURATION = 4000L
    }
}