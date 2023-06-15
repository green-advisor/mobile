package com.riski.greenadvisor.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CameraResponse(

	@field:SerializedName("data")
	val data: List<DataItemCamera>,

	@field:SerializedName("nama_tanaman")
	val namaTanaman: String,

	@field:SerializedName("status")
	val status: Boolean
)

@Parcelize
data class DataItemCamera(

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("nama_tanaman")
	val namaTanaman: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("cara_perawatan")
	val caraPerawatan: String,

	@field:SerializedName("referensi")
	val referensi: String
):Parcelable
