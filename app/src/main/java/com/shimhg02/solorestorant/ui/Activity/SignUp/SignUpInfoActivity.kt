package com.shimhg02.solorestorant.ui.Activity.SignUp


import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.Toast
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.utils.Preference.SharedPref
import gun0912.tedkeyboardobserver.BaseKeyboardObserver
import gun0912.tedkeyboardobserver.TedKeyboardObserver
import kotlinx.android.synthetic.main.activity_signup_info.*
import kotlinx.android.synthetic.main.activity_signup_info.ph_tv
import org.jetbrains.anko.startActivity
import java.util.*

class SignUpInfoActivity : AppCompatActivity() {
    val PREFERENCE = "com.shimhg02.honbab"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        SharedPref.openSharedPrep(this)
        setContentView(R.layout.activity_signup_info)
        keyBoardCheck()

        ph_tv.setText(pref.getString("phone_signup",""))
        ph_tv.inputType = InputType.TYPE_NULL

        boy_btn.setOnClickListener {
            boyClicked()
        }

        girl_btn.setOnClickListener {
            girlClicked()
        }

        date_picker.setOnClickListener {
            datePicker()
        }

        birt_rel.setOnClickListener {
            datePicker()
        }

        next_btn.setOnClickListener {
            next()
        }
    }

    fun CloseKeyboard()
    {
        var view = this.currentFocus

        if(view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun keyBoardCheck(){
        TedKeyboardObserver(this).listen(object : BaseKeyboardObserver.OnKeyboardListener {
            override fun onKeyboardChange(isShow: Boolean) {
                if(!isShow){
                    keyboard_index.visibility = View.GONE
                    first_key.visibility = View.VISIBLE
                }
                else{
                    keyboard_index.visibility = View.VISIBLE
                    first_key.visibility = View.GONE
                }
            }
        })
    }

    fun boyClicked(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("sex_signup", "남자")
        boy_btn.setBackgroundResource(R.drawable.signup_btn)
        boy_btn.setTextColor(Color.WHITE)
        girl_btn.setTextColor(Color.BLACK)
        girl_btn.setBackgroundResource(R.drawable.textinput_login)
        CloseKeyboard()
    }

    fun girlClicked(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("sex_signup","여자")
        girl_btn.setTextColor(Color.WHITE)
        boy_btn.setTextColor(Color.BLACK)
        girl_btn.setBackgroundResource(R.drawable.signup_btn)
        boy_btn.setBackgroundResource(R.drawable.textinput_login)
        CloseKeyboard()
    }

    fun datePicker(){
        var calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var secondDay = ""

        var date_listener  = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                if(dayOfMonth.toString().length == 1){
                    secondDay = "0"+dayOfMonth
                }
                else{
                    secondDay = dayOfMonth.toString()
                }
                birth_tv.text = "${year}-0${month+1}-${secondDay}"
            }
        }

        var builder = DatePickerDialog(this, date_listener, year, month, day)
        builder.show()
    }

    fun next(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
        if(ph_tv.text.toString() != "" && birth_tv.text.toString() != "" && email_tv.text.toString() != ""  && name_tv.text.toString() != ""){
            editor.putString("name_signup", name_tv.text.toString())
            editor.putString("birth_signup", birth_tv.text.toString())
            editor.putString("mail_signup", email_tv.text.toString())
            editor.commit()
            editor.apply()
            System.out.println("LOGD: " + pref.getString("name_signup","") + pref.getString("birth_signup","") + pref.getString("mail_signup","") + pref.getString("sex_signup",""))
            startActivity<SignUpIdActivity>()
        }

        else{
            Toast.makeText(this@SignUpInfoActivity, "입력란을 전부 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}