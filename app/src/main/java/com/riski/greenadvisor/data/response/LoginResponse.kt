package com.riski.greenadvisor.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: DataLogin?
)

data class DataLogin(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("token")
	val token: String,

	var isLogin: Boolean
)
