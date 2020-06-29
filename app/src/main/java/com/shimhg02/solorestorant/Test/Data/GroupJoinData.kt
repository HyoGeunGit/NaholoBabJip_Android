package com.shimhg02.solorestorant.Test.Data

data class GroupJoinData(
    val _id: String,
    val category: String,
    val groupName: String,
    val groupUUID: String,
    val isAdult: Boolean,
    val lat: String,
    val lng: String,
    val maximum: Int,
    val time: String,
    val users: List<User>,
    val vicinity: String
)