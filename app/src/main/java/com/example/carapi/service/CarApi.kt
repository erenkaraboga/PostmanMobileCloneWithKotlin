package com.example.carapi.service

import android.telecom.Call
import com.example.carapi.model.BrandModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CarApi {
     @GET("api/brands/ ")
    suspend fun getData() : retrofit2.Response<List<BrandModel>>

     @Multipart
     @POST("api/brands/")
   suspend  fun postData(
         @Part image : MultipartBody.Part,
         @Part("madeby") madeby:RequestBody,
         @Part("name") name:RequestBody
     ):retrofit2.Response<Void>

     @FormUrlEncoded
     @POST("api/brands/")
    suspend fun postDataNoImage(
        @Field("madeby") madeby : String,
        @Field("name") name : String,

    ):retrofit2.Response<Void>

    @FormUrlEncoded
    @PUT("api/brands/{id}")
   suspend fun updateDataNoImage(
        @Path("id") id : Int,
        @Field("madeby") madeby : String,
        @Field("name") name : String,
        ):retrofit2.Response<Void>

    @Multipart
    @PUT("api/brands/{id}")
   suspend fun updateData(
        @Path("id") id : Int,
        @Part image : MultipartBody.Part,
        @Part("madeby") madeby:RequestBody,
        @Part("name") name:RequestBody
    ):retrofit2.Response<Void>

     @DELETE("api/brands/{id}")
    suspend fun delete(@Path("id") id : Int
     ):retrofit2.Response<Void>

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