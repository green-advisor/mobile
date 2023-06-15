package com.riski.greenadvisor.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MapsResponse(

	@field:SerializedName("data")
	val data: List<DataItemMaps>,

	@field:SerializedName("status")
	val status: String
)

@Parcelize
data class DataItemMaps(

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("nama_tanaman")
	val namaTanaman: String,

	@field:SerializedName("iklim")
	val iklim: String

): Parcelable
