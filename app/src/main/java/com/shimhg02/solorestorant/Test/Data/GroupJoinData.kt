package com.shimhg02.solorestorant.Test.Data

/**
 * @description 그룹 참여 데이터
 */

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