package com.shimhg02.solorestorant.network.Data.GroupData

/**
 * @description 그룹 데이터
 */

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