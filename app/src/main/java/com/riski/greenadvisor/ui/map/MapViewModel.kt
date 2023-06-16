package com.riski.greenadvisor.ui.map

import android.util.Log
import androidx.lifecycle.*
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.api.ApiConfig
import com.riski.greenadvisor.data.response.DataItemMaps
import com.riski.greenadvisor.data.response.MapsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(private val preference: Preference) : ViewModel() {
    private val _respone = MutableLiveData<MapsResponse?>()
    val response: LiveData<MapsResponse?> = _respone

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _getDataMaps = MutableLiveData<List<DataItemMaps>>()
    val getDataMaps: LiveData<List<DataItemMaps>> = _getDataMaps

    fun mapsPlant(token: String, longitude: Int, latitude: Int) {
        _loading.value = true
        _respone.value = null

        val api = ApiConfig().getApiElevation().getAllMaps("Bearer $token", longitude, latitude)
        api.enqueue(object : Callback<MapsResponse> {
            override fun onResponse(call: Call<MapsResponse>, response1: Response<MapsResponse>) {
                _loading.value = false
                if (response1.isSuccessful && response1.body() != null) {
                    _getDataMaps.value = response1.body()?.data
                    _respone.value = response1.body()
                } else {
                    Log.e(TAG, "onFailure: ${response1.message()}")
                }
            }

            override fun onFailure(call: Call<MapsResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }
}