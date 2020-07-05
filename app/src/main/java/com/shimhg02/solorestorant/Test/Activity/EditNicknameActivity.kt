package com.shimhg02.solorestorant.Test.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Retrofit.Client
import kotlinx.android.synthetic.main.activity_edit_nickname.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditNicknameActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_nickname)

        edit_nick_btn.setOnClickListener{
            editNick()
        }
    }

    private fun editNick() {
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        Client.retrofitService.changeNick(pref.getString("token","").toString(), nick_tv.text.toString()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                when (response!!.code()) {
                    200 -> {
                        Toast.makeText(this@EditNicknameActivity,"Success",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {

            }
        })
    }
}