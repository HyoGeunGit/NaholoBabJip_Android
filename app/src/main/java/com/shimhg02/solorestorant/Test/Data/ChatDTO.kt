package com.shimhg02.solorestorant.Test.Data

class ChatDTO {
    var userName: String? = null
    var message: String? = null

    constructor() {} //이거 지우면 이니셜라이징 안되서 큰일나요 흑흑 이거 하나땜에 고생했어요
    constructor(userName: String?, message: String?) {
        this.userName = userName
        this.message = message
    }

}
