package com.example.myapplication.model

import com.google.gson.annotations.SerializedName


data class Post(
    val name: String,
    val description: String,
    val picture: String?, // Nullable since some entries have null
    val rating: Double,
    val address: String,
    val coordinates: List<Double>,
    @SerializedName("google_maps_link")
    val googleMapsLink: String,
    val website: String
)
