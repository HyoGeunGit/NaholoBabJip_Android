package com.shimhg02.solorestorant.ui.Activity.LogIn


import android.widget.Toast
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.LogIn
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Main.MainActivity
import com.shimhg02.solorestorant.ui.Activity.SignUp.SignUpPhoneActivity
import com.shimhg02.solorestorant.utils.Bases.BaseActivity
import com.shimhg02.solorestorant.utils.Preference.SharedPref
import org.jetbrains.anko.startActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : BaseActivity() {
    val PREFERENCE = "com.shimhg02.honbab"
    override var viewId: Int = R.layout.activity_login

    override fun onCreate() {
        SharedPref.openSharedPrep(this)
        AutoLogin()
        login_btn.setOnClickListener { login() }
        signup_go.setOnClickListener {  startActivity<SignUpPhoneActivity>() }
    }


    fun AutoLogin(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        if (pref.getString("token", "").toString() !== "") {
            startActivity<MainActivity>()
        }
    }

    fun login() {
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
            Client.retrofitService.logIn(id_tv.text.toString(), pw_tv.text.toString()).enqueue(object : Callback<LogIn> {
            override fun onResponse(call: Call<LogIn>?, response: Response<LogIn>?) {
                when (response!!.code()) {
                    200 -> {
                        editor.putString("userName", response.body()!!.name.toString())
                        editor.putString("token", response.body()!!.token.toString())
                        editor.apply()
                        startActivity<MainActivity>()
                        finish()
                    }
                    207 -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "약관에 동의해주세요.",
                            Toast.LENGTH_LONG).show()
                    }
                    404 -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "로그인 실패: PW나 ID를 다시 확인하세요.",
                            Toast.LENGTH_LONG).show()
                    }
                    500 -> Toast.makeText(this@LoginActivity, "서버에러", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<LogIn>?, t: Throwable?) {

            }
        })

    }

}