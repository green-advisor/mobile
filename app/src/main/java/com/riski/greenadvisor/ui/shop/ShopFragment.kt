package com.riski.greenadvisor.ui.shop

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.riski.greenadvisor.BetaActivity
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.response.DataLogin
import com.riski.greenadvisor.databinding.FragmentShopBinding
import com.riski.greenadvisor.ui.home.HomeViewModel
import com.riski.greenadvisor.ui.home.HomeViewModelFactory
import com.riski.greenadvisor.ui.home.dataStore
import com.riski.greenadvisor.ui.shop.adapter.BannerAdapter
import com.riski.greenadvisor.ui.shop.adapter.ShopAdapter

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class ShopFragment : Fragment() {
    private lateinit var dataStore: DataStore<Preferences>
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding
    private lateinit var homeShopViewModel: HomeViewModel
    private lateinit var shopViewModel: ShopViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var user: DataLogin
    private lateinit var adapter: ShopAdapter

    @Suppress("DEPRECATION")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.shop)
            show()
        }

        shopViewModel1()
        homeViewModel1()
        setBannerList()
        shopRecyclerView()
        searchBar()
        progressBar()

        return binding?.root!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataStore = requireContext().dataStore
    }

    private fun shopViewModel1() {
        shopViewModel = ViewModelProvider(this, ShopViewModelFactory(Preference.getInstance(dataStore), requireContext()))[ShopViewModel::class.java]

    }

    private fun homeViewModel1() {
        homeShopViewModel = ViewModelProvider(this, HomeViewModelFactory(Preference.getInstance(dataStore), requireContext()))[HomeViewModel::class.java]

        homeShopViewModel.getUser().observe(requireActivity()) {
            user = DataLogin(
                it.name,
                it.token,
                true
            )
            shopRecyclerView()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun shopRecyclerView() {
        adapter = ShopAdapter()
        binding?.shopRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
        binding?.shopRecyclerView?.setHasFixedSize(true)
        binding?.shopRecyclerView?.adapter = adapter
        shopViewModel.getAllShop(user.token)
        shopViewModel.getData.observe(requireActivity()) {
            adapter.setShopData(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setBannerList() {
        viewPager = binding?.shopViewpager!!
        val bannerAdapter = BannerAdapter(this)
        viewPager.adapter = bannerAdapter
        bannerAdapter.startAutoSwipe(viewPager)
    }

    private fun progressBar() {
        shopViewModel.loading.observe(requireActivity()) {
            binding?.apply {
                if (it) {
                    shopLoading.visibility = View.VISIBLE
                    shopRecyclerView.visibility = View.INVISIBLE
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        shopLoading.visibility = View.GONE
                        shopRecyclerView.visibility = View.VISIBLE
                    }, 1500L)
                }
            }
        }
    }

    private fun searchBar() {
        binding?.shopSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                shopViewModel.getData
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
             return false
            }
        })
        }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shop, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.shop_cart -> {
                startActivity(Intent(requireContext(), BetaActivity::class.java))
            }
        }
    }
}