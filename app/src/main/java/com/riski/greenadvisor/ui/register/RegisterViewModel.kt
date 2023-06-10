package com.riski.greenadvisor.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.greenadvisor.data.api.ApiConfig
import com.riski.greenadvisor.data.response.DataRegister
import com.riski.greenadvisor.data.response.RegisterResponse
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {

    private val _response = MutableLiveData<RegisterResponse?>()
    val response : LiveData<RegisterResponse?> = _response

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    fun register(name: String, email: String, password: String, cPassword: String) {
        _loading.value = true
        _response.value = null
        val api = ApiConfig.getApiService().register(name, email, password, cPassword)
        api.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response1: Response<RegisterResponse>) {
                _loading.value = false
                if (response1.isSuccessful && response1.body() != null) {
                    _response.value = response1.body()
                } else {
                    val jsonObject = JSONObject(response1.errorBody()!!.charStream().readText())
                    val success = jsonObject.getBoolean("success")
                    val message = jsonObject.getString("message")
                    val data = DataRegister(
                        name = jsonObject.optString("name"),
                        email = jsonObject.optJSONArray("email")?.let { jsonArrayToList(it) },
                        password = jsonObject.optJSONArray("password")?.let { jsonArrayToList(it) },
                        cPassword = jsonObject.optJSONArray("c_password")?.let { jsonArrayToList(it) }
                    )
                    val registerResponse = RegisterResponse(data, success, message)
                    _response.value = registerResponse
                    Log.e(TAG, "onFailure: ${response1.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun jsonArrayToList(jsonArray: JSONArray): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getString(i)
            list.add(item)
        }
        return list
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}