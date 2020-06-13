package com.shimhg02.solorestorant.ui.Activity.SignUp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.ui.Activity.LogIn.LoginActivity
import kotlinx.android.synthetic.main.activity_signup_phone.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.startActivity


class SignUpPhoneActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_phone)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val edit = findViewById(R.id.ph_tv) as EditText
        edit.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s:CharSequence, start:Int, before:Int, count:Int) {
                if(ph_tv.text.contains("-")){
                    ph_tv.text.toString().replace("-"," ")
                    Toast.makeText(this@SignUpPhoneActivity,"-를 제외하고 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                if(ph_tv.length()==11){
                    send_btn.setBackgroundResource(R.drawable.signup_btn)
                }
                else{
                    send_btn.setBackgroundResource(R.drawable.not_access_btn)
                }
            }
            override fun afterTextChanged(arg0: Editable) {
                val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                val editor = pref.edit()
                send_btn.setOnClickListener {
                    if(access_tv.text.toString() == "1111"){
                        send_btn.setOnClickListener {
                            Toast.makeText(
                                this@SignUpPhoneActivity,
                                "인증되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@SignUpPhoneActivity, SignUpInfoActivity::class.java))
                            finish()
                        }
                    }
                    if(visible_access.visibility != View.VISIBLE){

                        if(ph_tv.length() !== 11){
                            Toast.makeText(
                                this@SignUpPhoneActivity,
                                "정확한 핸드폰 번호를 입력해 주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else{
                            Toast.makeText(
                                this@SignUpPhoneActivity,
                                "${ph_tv.text}로 인증번호를 발송했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            ph_tv.inputType = InputType.TYPE_NULL
                            ph_tv.isFocusable = false
                            ph_tv.isClickable = false
                            send_btn.text = "인증하기"
                            ph_tf.backgroundResource = R.drawable.textinput_locked
                            access_tv.visibility = View.VISIBLE
                            access_show.visibility = View.VISIBLE
                            visible_access.visibility = View.VISIBLE
                            editor.putString("phone_signup", ph_tv.text.toString())
                            editor.commit()
                            editor.apply()
                            access_tv.requestFocus()
                        }
                    }

                }
                // 입력이 끝났을 때

            }
            override fun beforeTextChanged(s:CharSequence, start:Int, count:Int, after:Int) {
                // 입력하기 전에
                send_btn.setOnClickListener {
                    Toast.makeText(
                        this@SignUpPhoneActivity,
                        "핸드폰 번호를 입력해 주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                send_btn.setBackgroundResource(R.drawable.not_access_btn)
            }
        })
        already_regi.setOnClickListener { finish() }

    }
}