package com.riski.greenadvisor.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riski.greenadvisor.data.Preference

class HomeViewModelFactory(private val preference: Preference, private val context: Context): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(preference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}