package com.shimhg02.solorestorant.Test.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shimhg02.solorestorant.R
import kotlinx.android.synthetic.main.activity_edit_nickname.*

class EditNicknameActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_nickname)

        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
    }
}