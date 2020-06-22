package com.shimhg02.solorestorant.network.Data


import com.google.gson.annotations.SerializedName

class LocationRepo {
    @SerializedName("lat")
    var lat = ""

    @SerializedName("lng")
    var lng = ""

    @SerializedName("name")
    var placeName = ""

    @SerializedName("photo")
    var placePhoto = ""

    @SerializedName("vicinity")
    var vicinity = ""
}