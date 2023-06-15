package com.riski.greenadvisor.ui.camera

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.api.ApiConfig
import com.riski.greenadvisor.data.response.CameraResponse
import com.riski.greenadvisor.data.response.DataItemCamera
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CameraViewModel(private val preference: Preference): ViewModel() {
    private val _response = MutableLiveData<CameraResponse?>()
    val response: LiveData<CameraResponse?> = _response

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _getData = MutableLiveData<List<DataItemCamera>>()
    val getData: LiveData<List<DataItemCamera>> = _getData

    fun getAllCamera(token: String, imgMultipart: MultipartBody.Part) {
        _loading.value = true
        _response.value = null

        val api = ApiConfig().getApiCamera().getAllCamera("Bearer $token", imgMultipart)
        api.enqueue(object : Callback<CameraResponse> {
            override fun onResponse(call: Call<CameraResponse>, response1: Response<CameraResponse>) {
                _loading.value = false
                if (response1.isSuccessful && response1.body() != null) {
                    _response.value = response1.body()
                    _getData.value = response1.body()?.data
                } else {
                    Log.e(TAG, "onFailure: ${response1.message()}")
                }
            }

            override fun onFailure(call: Call<CameraResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "CameraViewModel"
    }
}