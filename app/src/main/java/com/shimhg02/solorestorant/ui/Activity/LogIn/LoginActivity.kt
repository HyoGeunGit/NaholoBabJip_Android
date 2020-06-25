package com.shimhg02.solorestorant.ui.Activity.LogIn


import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Activity.TestInfoActivity
import com.shimhg02.solorestorant.network.Data.LogIn
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Main.MainActivity
import com.shimhg02.solorestorant.ui.Activity.SignUp.SignUpPhoneActivity
import com.shimhg02.solorestorant.ui.Activity.Term.TermActivity
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
            Client.retrofitService.autoLogin(pref.getString("token","").toString()).enqueue(object :
                Callback<LogIn> {
                override fun onResponse(call: Call<LogIn>?, response: Response<LogIn>?) {
                    when (response!!.code()) {
                        200 -> {
                            startActivity<MainActivity>()
                            finish()
                        }
                        203-> {
                            alertTermDialog()
                        }
                        500 -> Toast.makeText(this@LoginActivity, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<LogIn>?, t: Throwable?) {

                }
            })
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
                    203 -> {
                        alertTermDialog()
                    }
                    404 -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "로그인 실패: PW나 ID를 다시 확인하세요.",
                            Toast.LENGTH_LONG).show()
                    }
                    500 -> Toast.makeText(this@LoginActivity, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<LogIn>?, t: Throwable?) {

            }
        })

    }

    fun alertTermDialog(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
        var dialog = AlertDialog.Builder(this@LoginActivity)
        dialog.setTitle("약관 동의")
        dialog.setMessage("개인정보 수집 약관에 동의를 하지 않으셔서 로그인을 할 수 없습니다.\n동의 페이지로 넘어갈까요?")
        dialog.setIcon(R.mipmap.ic_launcher)


        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->{
                        startActivity<TermActivity>()
                        finish()
                    }
                    DialogInterface.BUTTON_NEGATIVE ->{
                        editor.clear()
                        Toast.makeText(this@LoginActivity, "개인정보 수집 거부 처리 되었습니다. \n동의하시고 서비스를 사용하시길 원하신다면 다시 로그인 해주세요. ", Toast.LENGTH_LONG).show()
                    }

                }
            }
        }

        dialog.setPositiveButton("네",dialog_listener)
        dialog.setNegativeButton("아니오",dialog_listener)
        dialog.show()

    }
}