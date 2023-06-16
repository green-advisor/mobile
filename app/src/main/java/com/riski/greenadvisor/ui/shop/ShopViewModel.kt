package com.riski.greenadvisor.ui.shop

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.api.ApiConfig
import com.riski.greenadvisor.data.response.DataItem
import com.riski.greenadvisor.data.response.ShopResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopViewModel(private val preference: Preference) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _getData = MutableLiveData<List<DataItem>>()
    val getData: LiveData<List<DataItem>>  = _getData

    private val _response = MutableLiveData<Boolean>()
    val response: LiveData<Boolean> = _response

    fun getAllShop(token: String) {
        _loading.value = true

        val api = ApiConfig().getApiElevation().getAllShops("Bearer $token")
        api.enqueue(object : Callback<ShopResponse> {
            override fun onResponse(call: Call<ShopResponse>, response1: Response<ShopResponse>) {
                _loading.value = false
                if (response1.isSuccessful && response1.body() != null) {
                    _getData.value = response1.body()?.data
                } else {
                    Log.e(TAG, "onFailure: ${response1.message()}")
                }
            }

            override fun onFailure(call: Call<ShopResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "ShopViewModel"
    }
}