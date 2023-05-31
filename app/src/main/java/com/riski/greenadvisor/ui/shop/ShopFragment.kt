package com.riski.greenadvisor.ui.shop

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.riski.greenadvisor.R
import com.riski.greenadvisor.databinding.FragmentShopBinding
import com.riski.greenadvisor.ui.shop.adapter.BannerAdapter

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var shopViewModel: ShopViewModel
    private lateinit var viewPager: ViewPager2


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        shopViewModel = ViewModelProvider(this)[ShopViewModel::class.java]
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.show()

        setBannerList()

        return binding.root
    }

    private fun setBannerList() {
        viewPager = binding.shopViewpager
        val adapter = BannerAdapter(this)
        viewPager.adapter = adapter
        adapter.startAutoSwipe(viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}