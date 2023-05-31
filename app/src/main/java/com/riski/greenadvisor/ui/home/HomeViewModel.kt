package com.riski.greenadvisor.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.greetings.ArticlesData
import com.riski.greenadvisor.utils.ArticlesDataList

class HomeViewModel : ViewModel() {
    private val _plantCareList = MutableLiveData<List<Int>>()
    val plantCareList: LiveData<List<Int>>
        get() = _plantCareList

    private val _articlesList = MutableLiveData<List<ArticlesData>>()
    val articlesList: LiveData<List<ArticlesData>>
    get() = _articlesList

    init {
        loadImages()
        loadArticles()

    }

    private fun loadImages() {
        val images = listOf(R.drawable.plant_care1, R.drawable.plant_care2)
        _plantCareList.value = images
    }

    private fun loadArticles() {
        val articlesData = ArticlesDataList.getArticles()
        _articlesList.value = articlesData
    }
}