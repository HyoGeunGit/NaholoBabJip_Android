package com.shimhg02.solorestorant.network.Data

/**
 * @description 맛집 위치정보 params
 */

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


    @SerializedName("place_id")
    var placeId = ""

    @SerializedName("vicinity")
    var vicinity = ""
}