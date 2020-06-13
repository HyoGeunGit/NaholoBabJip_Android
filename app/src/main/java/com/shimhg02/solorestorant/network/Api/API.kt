package com.shimhg02.solorestorant.network.Api

import com.shimhg02.solorestorant.network.Data.LogIn
import retrofit2.Call
import retrofit2.http.*
interface API {


    @POST("/signin")
    @FormUrlEncoded
    fun logIn(@Field("id") id : String, @Field("passwd") pw : String) :  Call<LogIn>

    @POST("/signup")
    @FormUrlEncoded
    fun logUp(@Field("name") name: String?,
              @Field("id") id: String?,
              @Field("passwd") pw: String?,
              @Field("phoneNum") phoneNum: String?,
              @Field("birth") birth: String?,
              @Field("email") email: String?,
              @Field("nick") nickName: String?,
              @Field("sex") sex: String?) : Call<LogIn>

    @POST("user/termAccept")
    @FormUrlEncoded
    fun term(@Field("token") token : String, @Field("termAccept") accept : Boolean) :  Call<LogIn>

}