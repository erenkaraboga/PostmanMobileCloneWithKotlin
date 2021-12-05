package com.example.carapi.service

import android.telecom.Call
import com.example.carapi.model.BrandModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CarApi {
    //https://carrestfulapi.azurewebsites.net/
    // api/brands
     @GET("api/brands/ ")
     fun getData() : retrofit2.Call<List<BrandModel>>

     @Multipart
     @POST("api/brands/")
     fun postData(
         @Part image : MultipartBody.Part,
         @Part("madeby") madeby:RequestBody,
         @Part("name") name:RequestBody
     ):retrofit2.Call<RequestBody>

     @FormUrlEncoded
     @POST("api/brands/")
     fun postDataNoImage(
        @Field("madeby") madeby : String,
        @Field("name") name : String,

    ):retrofit2.Call<RequestBody>

    @FormUrlEncoded
    @PUT("api/brands/{id}")
    fun updateDataNoImage(
        @Path("id") id : Int,
        @Field("madeby") madeby : String,
        @Field("name") name : String,

        ):retrofit2.Call<RequestBody>

    @Multipart
    @PUT("api/brands/{id}")
    fun updateData(
        @Path("id") id : Int,
        @Part image : MultipartBody.Part,
        @Part("madeby") madeby:RequestBody,
        @Part("name") name:RequestBody
    ):retrofit2.Call<RequestBody>

     @DELETE("api/brands/{id}")
     fun delete(@Path("id") id : Int
     ):retrofit2.Call<Unit>

     companion object {
        operator fun invoke(): CarApi {
            return Retrofit.Builder()
                .baseUrl("https://carrestfulapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CarApi::class.java)
        }
    }
}