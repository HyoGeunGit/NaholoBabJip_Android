package com.shimhg02.solorestorant.Test.Adapter

data class TestInfoData(
    val formatted_phone_number: String,
    val name: String,
    val openNow: Boolean,
    val openTime: String,
    val photo: List<String>,
    val price_level: Int,
    val rating: Double,
    val reviews: List<Review>,
    val vicinity: String
)