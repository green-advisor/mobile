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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.riski.greenadvisor.R
import com.riski.greenadvisor.databinding.FragmentMapBinding
import com.riski.greenadvisor.ui.detail.detailarticles.DetailArticlesActivity
import com.riski.greenadvisor.ui.detail.showlist.DetailShowListArticlesActivity
import com.riski.greenadvisor.ui.home.adapter.ArticlesAdapter

class MapFragment : Fragment(), OnMapReadyCallback {

    private val _binding: FragmentMapBinding by lazy {
        FragmentMapBinding.inflate(layoutInflater)
    }
    private lateinit var mMap: GoogleMap
    private lateinit var bottomSlide: View
    private var isLocation: Boolean = false
    private lateinit var rootView: View
    private lateinit var bottomClick: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        rootView = _binding.root
        (activity as AppCompatActivity).supportActionBar?.show()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomSlide = inflater.inflate(R.layout.bottom_slide_layout_map, container, false)

        (rootView.parent as? ViewGroup)?.removeView(rootView)
        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getMyLocation()
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
            startActivity(Intent(requireContext(), DetailShowListArticlesActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val parentView = rootView as ViewGroup
        parentView.removeView(bottomSlide)
    }

}