package com.shimhg02.solorestorant.ui.Activity.Term


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.LoginData.LogIn
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Main.MainActivity
import com.shimhg02.solorestorant.utils.Preference.SharedPref
import kotlinx.android.synthetic.main.activity_terms.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @description 텀 액티비티
 */


class TermActivity : AppCompatActivity() {
    val PREFERENCE = "com.shimhg02.honbab"
    var checked = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        acceptClick()
        termCheck()
    }

    fun termAccept(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        SharedPref.openSharedPrep(this)
        Client.retrofitService.term(pref.getString("token","").toString(), true).enqueue(object :
            Callback<LogIn> {
            override fun onResponse(call: Call<LogIn>?, response: Response<LogIn>?) {
                when (response!!.code()) {
                    200 -> {
                        startActivity<MainActivity>()
                        finish()
                    }
                    404 -> {
                        Toast.makeText(
                            this@TermActivity,
                            "서버 점검중입니다. 잠시 후 다시 시도해 주세요.",
                            Toast.LENGTH_LONG).show()
                    }
                    500 -> Toast.makeText(this@TermActivity, "서버에러", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<LogIn>?, t: Throwable?) {

            }
        })
    }

    fun acceptClick(){
        access_radio.setOnClickListener {
            if(checked == 0){
                checked++
                access_radio.isChecked = true
                term_ok_btn.setBackgroundResource(R.drawable.signup_btn)
            }
            else{
                checked--
                access_radio.isChecked = false
                term_ok_btn.setBackgroundResource(R.drawable.not_access_btn)
            }
        }
    }

    fun termCheck(){
        term_ok_btn.setOnClickListener {
            if(access_radio.isChecked){
                termAccept()
            }
            else{
                Toast.makeText(this,"개인정보 수집 동의 항목에 체크해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

}