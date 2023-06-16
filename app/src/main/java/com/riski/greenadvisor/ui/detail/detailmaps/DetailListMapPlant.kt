package com.riski.greenadvisor.ui.detail.detailmaps

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.response.DataLogin
import com.riski.greenadvisor.databinding.ActivityDetailListMapPlantBinding
import com.riski.greenadvisor.ui.detail.adapter.DetailListMapPlantAdaptor
import com.riski.greenadvisor.ui.home.HomeViewModel
import com.riski.greenadvisor.ui.home.HomeViewModelFactory
import com.riski.greenadvisor.ui.map.MapViewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class DetailListMapPlant : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListMapPlantBinding
    private lateinit var homeMapsViewModel: HomeViewModel
    private lateinit var mapsViewModel: MapViewModel
    private lateinit var user: DataLogin
    private lateinit var adapter: DetailListMapPlantAdaptor

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailListMapPlantBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.detail_list_map)
            setDisplayHomeAsUpEnabled(true)
        }

        homeMapsViewModel1()
        mapsViewModel1()

        progressBar()

    }

    private fun mapsViewModel1() {
        mapsViewModel = ViewModelProvider(this, DetailListMapPlantFactory(Preference.getInstance(dataStore), this))[MapViewModel::class.java]
    }

    private fun homeMapsViewModel1() {
        homeMapsViewModel = ViewModelProvider(this, HomeViewModelFactory(Preference.getInstance(dataStore),this))[HomeViewModel::class.java]

        homeMapsViewModel.getUser().observe(this) {
            user = DataLogin(
                it.name,
                it.token,
                true
            )
            val latitude = intent.getIntExtra("latitude",0)
            val longitude = intent.getIntExtra("longitude",0)

            setRecyclerView(longitude, latitude)
        }
    }

    private fun setRecyclerView(longitude: Int,latitude: Int) {
        adapter = DetailListMapPlantAdaptor()
        binding.detailListMapsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.detailListMapsRecyclerView.setHasFixedSize(true)
        binding.detailListMapsRecyclerView.adapter = adapter
        mapsViewModel.mapsPlant(user.token, longitude, latitude)
        mapsViewModel.getDataMaps.observe(this) {
            adapter.setMapsData(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun progressBar() {
        mapsViewModel.loading.observe(this) {
            binding.apply {
                if (it) {
                    detailListMapsLoading.visibility = View.VISIBLE
                    detailListMapsRecyclerView.visibility = View.GONE
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        detailListMapsLoading.visibility = View.GONE
                        detailListMapsRecyclerView.visibility = View.VISIBLE
                    }, 1500L)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}