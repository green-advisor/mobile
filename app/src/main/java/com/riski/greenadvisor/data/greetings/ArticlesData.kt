package com.riski.greenadvisor.data.greetings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticlesData(
    val judul: String = "",
    val kategori: String = "",
    val deskripsi: String = "",
    val tahun: String = "",
    var imageId: Int = 0,
    val copyRight: String = ""
):Parcelable

//val imageId: Int,
//val title: String,
//val description: String