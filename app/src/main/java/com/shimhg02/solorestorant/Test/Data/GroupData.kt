package com.shimhg02.solorestorant.Test.Data

data class GroupData(
    val category: String,
    val groupName: String,
    val isAdult: Boolean,
    val lat: String,
    val lng: String,
    val maximum: Int,
    val time: String,
    val users: List<Any>,
    val vicinity: String,
    val groupUUID: String
)