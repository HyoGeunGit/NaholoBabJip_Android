package com.shimhg02.solorestorant.Test.Data

import com.google.firebase.database.Exclude


/**
 * Created by Alessandro Barreto on 22/06/2016.
 */
class UserModel {
    @get:Exclude
    var id: String? = null
    var name: String? = null
    var photo_profile: String? = null

    constructor() {}
    constructor(name: String?, photo_profile: String?, id: String?) {
        this.name = name
        this.photo_profile = photo_profile
        this.id = id
    }

}