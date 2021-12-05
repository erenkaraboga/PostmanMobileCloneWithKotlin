package com.example.carapi.model

import com.google.gson.annotations.SerializedName

data class BrandModel (
    @SerializedName("id")
    var id : Int,
    @SerializedName("madeby")
    var madeby : String,
    @SerializedName("name")
    var name : String,
    @SerializedName("imageUrl")
    var imageUrl : String,
    @SerializedName("audioUrl")
    var audioUrl : String
)


