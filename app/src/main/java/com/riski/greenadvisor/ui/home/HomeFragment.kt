package com.riski.greenadvisor.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.UserData
import com.riski.greenadvisor.data.greetings.ArticlesData
import com.riski.greenadvisor.databinding.FragmentHomeBinding
import com.riski.greenadvisor.ui.detail.showlist.DetailShowListArticlesActivity
import com.riski.greenadvisor.ui.detail.showlist.DetailShowListPlantActivity
import com.riski.greenadvisor.ui.home.adapter.ArticlesAdapter
import com.riski.greenadvisor.ui.home.adapter.HomeViewModelAdapter
import com.riski.greenadvisor.ui.home.adapter.PlantCareAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as AppCompatActivity).supportActionBar?.hide()
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
//        (activity as AppCompatActivity).invalidateOptionsMenu()
        setViewPager()
        changeName()
        setArticles()
        setPlantList()
        clickArticles()
        clickPlantCare()

        return root

    }

    private fun setViewPager() {
        viewPager = binding.homeViewpager
        val adapter =  HomeViewModelAdapter(this)
        viewPager.adapter = adapter
        adapter.startAutoSwipe(viewPager)
    }

    @SuppressLint("SetTextI18n")
    private fun changeName() {
        val user = UserData(getString(R.string.name))
        val welcomeText: TextView = binding.homeGreetings
        val welcomeMessage = getString(R.string.greetings3, user.name)
        welcomeText.text = welcomeMessage
    }

    private fun setPlantList() {
            val recyclerView: RecyclerView = binding.homeRecyclerViewPlant
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            homeViewModel.plantCareList.observe(viewLifecycleOwner) { plantCareList ->
            val plantCareAdapter = PlantCareAdapter(plantCareList)
            recyclerView.adapter = plantCareAdapter
        }
    }

    private fun setArticles() {
        val recyclerView: RecyclerView = binding.homeRecyclerViewArticles
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        homeViewModel.articlesList.observe(viewLifecycleOwner) { articlesList ->
            val slideArticles = articlesList.take(3)
            val articlesAdapter = ArticlesAdapter(slideArticles as ArrayList<ArticlesData>)
            recyclerView.adapter = articlesAdapter
        }
    }

    private fun clickPlantCare() {
        binding.homeSeeAllPlant.setOnClickListener {
            startActivity(Intent(requireContext(), DetailShowListPlantActivity::class.java))
        }
    }

    private fun clickArticles() {
        binding.homeSeeAllArticles.setOnClickListener {
            val intent = Intent(requireContext(), DetailShowListArticlesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}