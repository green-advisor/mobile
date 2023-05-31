package com.riski.greenadvisor.ui.shop.adapter

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.riski.greenadvisor.R
import com.riski.greenadvisor.ui.home.SlideFragment
import java.util.*

class BannerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private var currentPosition = 0
    private var timer: Timer? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var viewPager: ViewPager2

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        return when (position % 3) {
            0 -> SlideFragment.newInstance(R.layout.item_shop_banner)
            1 -> SlideFragment.newInstance(R.layout.item_shop_banner2)
            2 -> SlideFragment.newInstance(R.layout.item_shop_banner3)
            else -> SlideFragment.newInstance(R.layout.item_shop_banner)
        }
    }

    fun startAutoSwipe(viewPager: ViewPager2) {
        this.viewPager = viewPager
        viewPager.adapter = this

        stopAutoSwipe()

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                currentPosition = (currentPosition + 1) % itemCount
                handler.post {
                    viewPager.setCurrentItem(currentPosition, true)
                }
            }
        }, TIMER, TIMER)
    }

    private fun stopAutoSwipe() {
        timer?.cancel()
        timer = null
    }

    companion object {
        private const val TIMER = 4000L
    }
}