package com.shimhg02.solorestorant.network.Data.ChatData

import com.shimhg02.solorestorant.network.Data.ChatData.ImageFileModel.FileModel
import com.shimhg02.solorestorant.network.Data.ChatData.LocationFileModel.MapModel


/**
 * @description 채팅 모델
 */

class ChatModel {
    var id: String? = null
    private var userModel: UserModel? = null
    var message: String? = null
    var timeStamp: String? = null
    private var file: FileModel? = null
    private var mapModel: MapModel? = null

    constructor() {}
    constructor(
        userModel: UserModel?,
        message: String?,
        timeStamp: String?,
        file: FileModel?
    ) {
        this.userModel = userModel
        this.message = message
        this.timeStamp = timeStamp
        this.file = file
    }

    constructor(userModel: UserModel?, timeStamp: String?, mapModel: MapModel?) {
        this.userModel = userModel
        this.timeStamp = timeStamp
        this.mapModel = mapModel
    }

    fun getUserModel(): UserModel? {
        return userModel
    }

    fun setUserModel(userModel: UserModel?) {
        this.userModel = userModel
    }

    fun getFile(): FileModel? {
        return file
    }

    fun setFile(file: FileModel?) {
        this.file = file
    }

    fun getMapModel(): MapModel? {
        return mapModel
    }

    fun setMapModel(mapModel: MapModel?) {
        this.mapModel = mapModel
    }

    override fun toString(): String {
        return "ChatModel{" +
                "mapModel=" + mapModel +
                ", file=" + file +
                ", timeStamp='" + timeStamp + '\'' +
                ", message='" + message + '\'' +
                ", userModel=" + userModel +
                '}'
    }
}