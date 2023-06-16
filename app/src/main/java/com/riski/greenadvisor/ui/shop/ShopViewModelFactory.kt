package com.riski.greenadvisor.ui.shop

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riski.greenadvisor.data.Preference

class ShopViewModelFactory(private val preferences: Preference, private val context: Context): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopViewModel::class.java)) {
            return ShopViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}