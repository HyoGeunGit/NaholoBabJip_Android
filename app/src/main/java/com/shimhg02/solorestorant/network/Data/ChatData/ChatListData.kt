package com.shimhg02.solorestorant.network.Data.ChatData

data class ChatListData(
    val groupName: String,
    val groupUUID: String,
    val isAdult: Boolean,
    val lastMessage: String,
    val timeStamp: String
)