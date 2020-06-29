package com.shimhg02.solorestorant.Test.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Data.GroupData
import com.shimhg02.solorestorant.network.Retrofit.Client
import kotlinx.android.synthetic.main.activity_addgroup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGroupActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addgroup)
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
        send_btn.setOnClickListener {
            Client.retrofitService.addGroup(pref.getString("token","").toString(),name_tv.text.toString(),people_tv.text.toString(),0,0,vicinity_tv.text.toString(),time_tv.text.toString(),true, category_tv.text.toString()).enqueue(object : Callback<GroupData> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<GroupData>?, response: Response<GroupData>?) {
                    when (response!!.code()) {
                        200 -> {

                        }
                        203-> {
                        }
                    }
                }
                override fun onFailure(call: Call<GroupData>?, t: Throwable?) {

                }
            })
        }

    }
}