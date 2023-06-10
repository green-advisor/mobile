package com.riski.greenadvisor.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.data.api.ApiConfig
import com.riski.greenadvisor.data.response.LoginResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val preference: Preference): ViewModel() {
    private val _response = MutableLiveData<LoginResponse?>()
    val response: LiveData<LoginResponse?> = _response

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun login(email: String, password: String) {
        _loading.value = true
        _response.value = null

        val api = ApiConfig().getApiService().login(email, password)
        api.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response1: Response<LoginResponse>) {
                _loading.value = false
                if (response1.isSuccessful) {
                    _response.value = response1.body()
                    viewModelScope.launch {
                        response1.body()?.data?.isLogin = true
                        response1.body()?.data?.let { preference.saveLogin(it) }
                    }
                } else {
                    val jsonObj = JSONObject(response1.errorBody()!!.charStream().readText())
                    _response.value = LoginResponse(jsonObj.getBoolean("success"), jsonObj.getString("message"), null)
                    Log.e(TAG, "onFailure: ${response1.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}