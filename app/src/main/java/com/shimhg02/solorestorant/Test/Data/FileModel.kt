package com.shimhg02.solorestorant.Test.Data


/**
 * @description 파일모델 (파이어베이스 이미지/동영상 전송)
 */

class FileModel {
    var type: String? = null
    var url_file: String? = null
    var name_file: String? = null
    var size_file: String? = null

    constructor() {}
    constructor(
        type: String?,
        url_file: String?,
        name_file: String?,
        size_file: String?
    ) {
        this.type = type
        this.url_file = url_file
        this.name_file = name_file
        this.size_file = size_file
    }

}