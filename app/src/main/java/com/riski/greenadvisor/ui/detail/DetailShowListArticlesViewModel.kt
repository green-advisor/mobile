package com.riski.greenadvisor.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.greenadvisor.data.greetings.ArticlesData
import com.riski.greenadvisor.utils.ArticlesDataList

class DetailShowListArticlesViewModel: ViewModel() {
    private val _articlesList = MutableLiveData<List<ArticlesData>>()
    val articlesList: LiveData<List<ArticlesData>> = _articlesList

    init {
        loadArticles()
    }

    private fun loadArticles() {
        val articlesData = ArticlesDataList.getArticles()
        _articlesList.value = articlesData
    }
}