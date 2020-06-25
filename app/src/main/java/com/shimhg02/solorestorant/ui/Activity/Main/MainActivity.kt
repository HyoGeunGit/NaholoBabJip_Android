package com.shimhg02.solorestorant.ui.Activity.Main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Fragment.MapTestFragment
import com.shimhg02.solorestorant.Test.Fragment.TestFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.main_layout, TestFragment()).commitAllowingStateLoss()
        bottomNavigationView.setSelectedItemId(R.id.tab3);
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.tab1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, MapTestFragment())
                        .commitAllowingStateLoss()
                    title_toolbar.text = "혼밥하기 싫으면?"
                    appbar.visibility = View.VISIBLE
                    true
                }
                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, TestFragment())
                        .commitAllowingStateLoss()
                    title_toolbar.text = "채팅!"
                    appbar.visibility = View.VISIBLE
                    true
                }
                R.id.tab3 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, TestFragment())
                        .commitAllowingStateLoss()
                    title_toolbar.text = "피드!"
                    appbar.visibility = View.VISIBLE
                    true
                }
                R.id.tab4 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, TestFragment())
                        .commitAllowingStateLoss()
                    title_toolbar.text = "이벤트/쿠폰"
                    appbar.visibility = View.VISIBLE
                    true
                }
                R.id.tab5 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, TestFragment())
                        .commitAllowingStateLoss()
                    appbar.visibility = View.GONE
                    true
                }
                else -> false
            }
        })
    }
}