package com.riski.greenadvisor.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: DataRegister,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataRegister(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: List<String>?,

	@field:SerializedName("password")
	val password: List<String>?,

	@field:SerializedName("c_password")
	val cPassword: List<String>?

)
