package com.shimhg02.solorestorant.ui.Activity.Main


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.ui.Fragment.Feed.FeedFragment
import com.shimhg02.solorestorant.Test.Fragment.MapTestFragment
import com.shimhg02.solorestorant.ui.Fragment.Chat.ChatFragment
import com.shimhg02.solorestorant.ui.Fragment.Event.EventMainFragment
import com.shimhg02.solorestorant.ui.Fragment.Group.GroupFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @description 메인 액티비티
 */


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                       return@OnCompleteListener
//                }
//
//                // Get new Instance ID token
//                val token = task.result?.token
//
//                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//            })

        supportFragmentManager.beginTransaction().replace(R.id.main_layout,
            FeedFragment()
        ).commitAllowingStateLoss()
        bottomNavigationView.setSelectedItemId(R.id.tab3);
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.tab1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout,
                            GroupFragment()
                        )
                        .commitAllowingStateLoss()
                    title_toolbar.text = "혼밥하기 싫으면?"
                    appbar.visibility = View.VISIBLE
                    title_toolbar.height = 20
                    title_toolbar.visibility = View.VISIBLE
                    true
                }
                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, ChatFragment())
                        .commitAllowingStateLoss()
                    title_toolbar.text = "채팅!"
                    appbar.visibility = View.VISIBLE
                    title_toolbar.height = 40
                    title_toolbar.visibility = View.VISIBLE
                    true
                }
                R.id.tab3 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout,
                            FeedFragment()
                        )
                        .commitAllowingStateLoss()
                    title_toolbar.text = "피드!"
                    appbar.visibility = View.VISIBLE
                    title_toolbar.height = 40
                    title_toolbar.visibility = View.VISIBLE
                    true
                }
                R.id.tab4 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout,
                            EventMainFragment()
                        )
                        .commitAllowingStateLoss()
                    title_toolbar.text = "이벤트/쿠폰"
                    appbar.visibility = View.VISIBLE
                    title_toolbar.height = 20
                    title_toolbar.visibility = View.VISIBLE
                    true
                }
                R.id.tab5 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout,
                            FeedFragment()
                        )
                        .commitAllowingStateLoss()
                    title_toolbar.text = "설정"
                    title_toolbar.visibility = View.GONE
                    true
                }
                else -> false
            }
        })
    }
}