package com.riski.greenadvisor.data.api

import com.riski.greenadvisor.data.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("c_password") c_password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("elevation")
    fun getAllMaps(
        @Header("Authorization") auth: String,
        @Query("long") longitude: Int,
        @Query("lat") latitude: Int
    ): Call<MapsResponse>

    @GET("shops")
    fun getAllShops(@Header("Authorization") auth: String): Call<ShopResponse>

    @Multipart
    @POST("predict")
    fun getAllCamera(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part
    ): Call<CameraResponse>
}