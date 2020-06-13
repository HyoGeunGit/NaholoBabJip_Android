package com.shimhg02.solorestorant.ui.Activity.SignUp

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.utils.Preference.SharedPref
import kotlinx.android.synthetic.main.activity_signup_id.*
import org.jetbrains.anko.startActivity


class SignUpIdActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.openSharedPrep(this)
        setContentView(R.layout.activity_signup_id)

        duplicate_chk_btn.setOnClickListener {
            Toast.makeText(this,"사용할 수 있는 아이디 입니다!", Toast.LENGTH_SHORT).show()
        }

        checkPassWord()

    }

    fun checkPassWord(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
        val edit = findViewById(R.id.pw_chk_tv) as EditText
        edit.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (pw_tv.text.toString() != pw_chk_tv.text.toString()) {
                    chk_tv.text = "비밀번호가 일치하지 않습니다."
                    chk_tv.setTextColor(Color.RED)
                } else {
                    chk_tv.text = "일치합니다"
                    chk_tv.setTextColor(Color.GREEN)
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                next_btn.setOnClickListener {
                    if(chk_tv.text.toString() == "" || pw_tv.text.toString() == "" || id_tv.text.toString() == ""){
                        Toast.makeText(this@SignUpIdActivity, "입력란을 전부 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        next_btn.setOnClickListener {
                            editor.putString("id_signup",id_tv.text.toString())
                            editor.putString("id_signup",pw_tv.text.toString())
                            startActivity<SignUpProfileActivity>()
                        }
                    }
                }
            }

        })
    }
}