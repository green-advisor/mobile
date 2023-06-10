package com.riski.greenadvisor.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.riski.greenadvisor.BetaActivity
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.UserData
import com.riski.greenadvisor.data.greetings.ArticlesData
import com.riski.greenadvisor.data.response.DataLogin
import com.riski.greenadvisor.databinding.FragmentHomeBinding
import com.riski.greenadvisor.ui.detail.showlist.DetailShowListArticlesActivity
import com.riski.greenadvisor.ui.detail.showlist.DetailShowListPlantActivity
import com.riski.greenadvisor.ui.home.adapter.ArticlesAdapter
import com.riski.greenadvisor.ui.home.adapter.HomeViewModelAdapter
import com.riski.greenadvisor.ui.home.adapter.PlantCareAdapter
import com.riski.greenadvisor.ui.login.LoginActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class HomeFragment : Fragment() {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var user: DataLogin
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as AppCompatActivity).supportActionBar?.hide()
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
//        (activity as AppCompatActivity).invalidateOptionsMenu()
        homeUserViewModel()
        setViewPager()
        changeName()
        setArticles()
        setPlantList()
        clickArticles()
        clickPlantCare()
        notification()
        userLogin()


        return root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataStore = requireContext().dataStore
    }

    private fun homeUserViewModel() {
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(Preference.getInstance(dataStore), requireContext()))[HomeViewModel::class.java]
    }

    private fun userLogin() {
        val pref = Preference.getInstance(dataStore)
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(pref, requireContext()))[homeViewModel::class.java]
        homeViewModel.getUser().observe(requireActivity()) { user->
            this.user = user
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            } else {
                this.user = DataLogin(
                    user.name,
                    user.token,
                    true
                )
            }
        }
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
        val welcomeMessage = user.name
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

    private fun notification() {
        binding.homeNotification.setOnClickListener {
            startActivity(Intent(requireContext(), BetaActivity::class.java))
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