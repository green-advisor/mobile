package com.riski.greenadvisor.ui.greetings

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.riski.greenadvisor.R
import com.riski.greenadvisor.databinding.ActivityGreetingsBinding
import kotlin.math.roundToInt

@Suppress("NAME_SHADOWING")
class GreetingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGreetingsBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var greetingsViewModel: GreetingsViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGreetingsBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        greetingsViewModel = ViewModelProvider(this)[GreetingsViewModel::class.java]
        setupViewPagerWithIndicator()

    }

    private fun setupViewPagerWithIndicator() {
        viewPager = binding.greetingsViewpager2
        val indicatorLayout = binding.indicatorLayout
        val adapter = GreetingsAdapter(greetingsViewModel)
        viewPager.adapter = adapter

        val indicatorCount = adapter.itemCount
        for (i in 0 until indicatorCount) {
            val indicatorView = ImageView(this)
            indicatorView.setImageResource(R.drawable.ic_baseline_circle_24)
            val params = LinearLayout.LayoutParams(10.dpToPx(), 10.dpToPx())
            params.setMargins(8.dpToPx(), 0, 8.dpToPx(), 0)
            indicatorView.layoutParams = params
            indicatorLayout.addView(indicatorView)
        }
        binding.greetingsTeam.visibility = View.VISIBLE
        binding.greetingsCopyrightTeam.visibility = View.VISIBLE

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val indicatorCount = indicatorLayout.childCount
                for (i in 0 until indicatorCount) {
                    val indicatorView = indicatorLayout.getChildAt(i) as ImageView
                    if (i == position) {
                        indicatorView.setImageResource(R.drawable.ic_baseline_circle_24_unselected)
                    } else {
                        indicatorView.setImageResource(R.drawable.ic_baseline_circle_24)
                    }
                    val greetingsTeamTextView = binding.greetingsTeam
                    val greetingsCopy = binding.greetingsCopyrightTeam
                    if (position == 0) {
                        greetingsTeamTextView.visibility = View.VISIBLE
                        greetingsCopy.visibility = View.VISIBLE
                    } else {
                        greetingsTeamTextView.visibility = View.GONE
                        greetingsCopy.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun Int.dpToPx(): Int {
        val density =  Resources.getSystem().displayMetrics.density
        return (this * density).roundToInt()
    }
}