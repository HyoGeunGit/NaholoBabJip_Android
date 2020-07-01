package com.shimhg02.solorestorant.Test.Data

/**
 * @description 위치 모델
 */

class MapModel {
    var latitude: String? = null
    var longitude: String? = null

    constructor() {}
    constructor(latitude: String?, longitude: String?) {
        this.latitude = latitude
        this.longitude = longitude
    }

}