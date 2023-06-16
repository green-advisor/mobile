package com.riski.greenadvisor.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.response.DataLogin
import com.riski.greenadvisor.databinding.FragmentMapBinding
import com.riski.greenadvisor.ui.detail.detailmaps.DetailListMapPlant
import com.riski.greenadvisor.ui.home.HomeViewModel
import com.riski.greenadvisor.ui.home.HomeViewModelFactory
import com.riski.greenadvisor.ui.home.dataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class MapFragment : Fragment(), OnMapReadyCallback {

    private val _binding: FragmentMapBinding by lazy {
        FragmentMapBinding.inflate(layoutInflater)
    }
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var mMap: GoogleMap
    private lateinit var bottomSlide: View
    private var isLocation: Boolean = false
    private lateinit var rootView: View
    private lateinit var user : DataLogin
    private lateinit var bottomClick: TextView
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = _binding.root
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.maps)
            show()
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomSlide = inflater.inflate(R.layout.bottom_slide_layout_map, container, false)

        (rootView.parent as? ViewGroup)?.removeView(rootView)

        homeViewModel1()

        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        getMyLocation()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataStore = requireContext().dataStore
    }

    private fun homeViewModel1() {
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(Preference.getInstance(dataStore), requireContext()))[HomeViewModel::class.java]

        homeViewModel.getUser().observe(requireActivity()) {
            user = DataLogin(
                it.name,
                it.token,
                true
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            val provider = locationManager.getBestProvider(criteria, true)
            val location: Location? = provider?.let {
                locationManager.getLastKnownLocation(it)
            }

            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                showBottomSlide()
                isLocation = true
            }

        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showBottomSlide() {
        val slideUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        bottomSlide.startAnimation(slideUpAnimation)

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            resources.getDimension(R.dimen.bottom_slide_up).toInt()
        )
        layoutParams.gravity = Gravity.BOTTOM
        bottomSlide.layoutParams = layoutParams

        val parentView = rootView as ViewGroup
        parentView.addView(bottomSlide)

        bottomSlide.setOnTouchListener { v, event -> true }
        bottomClick = bottomSlide.findViewById(R.id.bottom_plant)
        bottomClick.setOnClickListener {
            val latitude = mMap.cameraPosition.target.latitude
            val longitude = mMap.cameraPosition.target.longitude

            val intent = Intent(requireContext(), DetailListMapPlant::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val parentView = rootView as ViewGroup
        parentView.removeView(bottomSlide)
    }

}