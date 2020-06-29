package com.shimhg02.solorestorant.ui.Activity.SignUp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.LogIn
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Term.TermActivity
import com.shimhg02.solorestorant.utils.Preference.SharedPref
import kotlinx.android.synthetic.main.activity_signup_profile.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpProfileActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_profile)
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        SharedPref.openSharedPrep(this)
        onBackPressed()
        if(pref.getBoolean("sex_signup",false) == false){
            profile_img.setImageResource(R.drawable.profile_male)
        }
        else{
            profile_img.setImageResource(R.drawable.profile_female)
        }
        next_btn.setOnClickListener {
            signup()
        }

    }

    fun signup(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        SharedPref.openSharedPrep(this)
        val editor = pref.edit()
        Client.retrofitService.logUp(
            pref.getString("name_signup",""),
            pref.getString("id_signup",""),
            pref.getString("pw_signup",""),
            pref.getString("phone_signup",""),
            pref.getString("birth_signup",""),
            pref.getString("mail_signup",""),
            nick_tv.text.toString(),
            pref.getBoolean("sex_signup", false)
        )
            .enqueue(object : Callback<LogIn> {
            override fun onResponse(call: Call<LogIn>?, response: Response<LogIn>?) {
                when (response!!.code()) {
                    200 -> {
                        editor.putString("userName", response.body()!!.name.toString())
                        editor.putString("token", response.body()!!.token.toString())
                        editor.apply()
                        startActivity<TermActivity>()
                        finish()
                    }
                    404 -> {
                        Toast.makeText(
                            this@SignUpProfileActivity,
                            "중복된 회원입니다.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    500 -> Toast.makeText(this@SignUpProfileActivity, "서버에러", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<LogIn>?, t: Throwable?) {

            }
        })
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}