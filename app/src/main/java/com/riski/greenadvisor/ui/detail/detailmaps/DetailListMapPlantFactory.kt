package com.riski.greenadvisor.ui.detail.detailmaps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.ui.map.MapViewModel

class DetailListMapPlantFactory(private val preferences: Preference, private val context: Context): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}