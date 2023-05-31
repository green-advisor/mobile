package com.riski.greenadvisor.ui.detail.showlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riski.greenadvisor.R
import com.riski.greenadvisor.databinding.ActivityDetailShowListArticlesBinding
import com.riski.greenadvisor.ui.detail.DetailShowListArticlesViewModel
import com.riski.greenadvisor.ui.detail.adapter.DetailShowListArticlesAdaptor

class DetailShowListArticlesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailShowListArticlesBinding
    private lateinit var detailarticlesViewModel: DetailShowListArticlesViewModel
    private lateinit var adapter: DetailShowListArticlesAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailShowListArticlesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.home_show_articles)
            setDisplayHomeAsUpEnabled(true)
        }

        detailarticlesViewModel = ViewModelProvider(this)[DetailShowListArticlesViewModel::class.java]
        adapter = DetailShowListArticlesAdaptor(arrayListOf())

        setArticles()
    }


    private fun setArticles() {
        val recyclerView: RecyclerView = binding.detailArticlesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        detailarticlesViewModel.articlesList.observe(this) {
            adapter.setListDetailArticles(it)
        }
      }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

   }