package com.shimhg02.solorestorant.network.Api

import com.shimhg02.solorestorant.Test.Adapter.TestInfoData
import com.shimhg02.solorestorant.network.Data.LocationRepo
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
              @Field("phone") phoneNum: String?,
              @Field("birth") birth: String?,
              @Field("email") email: String?,
              @Field("nick") nickName: String?,
              @Field("sex") sex: Boolean?) : Call<LogIn>

    @POST("/termsCheck")
    @FormUrlEncoded
    fun term(@Field("token") token : String, @Field("terms") accept : Boolean) :  Call<LogIn>

    @POST("/duplicateChk")
    @FormUrlEncoded
    fun duplicate(@Field("id") id : String) :  Call<Void>

    @POST("/autoLogin")
    @FormUrlEncoded
    fun autoLogin(@Field("token") token : String) :  Call<LogIn>

    @POST("/getPlace")
    @FormUrlEncoded
    fun getPlace(@Field("lat") lat : String, @Field("lng") lng : String, @Field("range") range : Number) :  Call<ArrayList<LocationRepo>>

    @POST("/getDetail")
    @FormUrlEncoded
    fun getDetail(@Field("place_id") placeid : String) :   Call<TestInfoData>

    @POST("/getCategory ")
    @FormUrlEncoded
    fun getCategory(@Field("lat") lat : String, @Field("lng") lng : String, @Field("range") range : Number, @Field("category") category : String) :  Call<ArrayList<LocationRepo>>

}