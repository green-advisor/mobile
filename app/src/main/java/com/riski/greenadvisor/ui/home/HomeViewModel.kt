package com.riski.greenadvisor.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.greetings.ArticlesData
import com.riski.greenadvisor.data.response.DataLogin
import com.riski.greenadvisor.utils.ArticlesDataList

class HomeViewModel(private val preference: Preference) : ViewModel() {
    private val _plantCareList = MutableLiveData<List<Int>>()
    val plantCareList: LiveData<List<Int>>
        get() = _plantCareList

    private val _articlesList = MutableLiveData<List<ArticlesData>>()
    val articlesList: LiveData<List<ArticlesData>>
    get() = _articlesList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        loadImages()
        loadArticles()

    }

    fun getUser(): LiveData<DataLogin> {
        return preference.getLogin().asLiveData()
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