package com.shimhg02.solorestorant.ui.Activity.Main

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Fragment.TestFragment
import com.shimhg02.solorestorant.utils.Bases.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override var viewId: Int = R.layout.activity_main
    private lateinit var toast: Toast
    private var backKeyPressedTime: Long = 200
    private var mViewPager: ViewPager? = null

    @SuppressLint("ShowToast")
    override fun onCreate() {
        onRestart()
        mViewPager = findViewById(R.id.viewPager)
        mViewPager!!.adapter = PagerAdapter(supportFragmentManager)
        mViewPager!!.currentItem = 2
        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
        tabLayout.tabRippleColor
        // set icons
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_launcher_foreground)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_launcher_foreground)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_launcher_background)
        tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_launcher_foreground)
        tabLayout.getTabAt(4)!!.setIcon(R.drawable.ic_launcher_foreground)
        mViewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab){
                val pos = tab.position
                if(pos==0){
                    tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_launcher_background)
                    title_toolbar.text = "혼밥하기 싫으면!"
                }
                if(pos==1){
                    tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_launcher_background)
                    title_toolbar.text = "채팅!"
                }
                if(pos==2){
                    tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_launcher_background)
                    title_toolbar.text = "피드!"
                }
                if(pos==3) {
                    tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_launcher_background)
                    title_toolbar.text = "이벤트/쿠폰!"
                }
                if(pos==4) {
                    tabLayout.getTabAt(4)!!.setIcon(R.drawable.ic_launcher_background)
                    title_toolbar.text = "설정!"
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_launcher_foreground)
                tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_launcher_foreground)
                tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_launcher_foreground)
                tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_launcher_foreground)
                tabLayout.getTabAt(4)!!.setIcon(R.drawable.ic_launcher_foreground)
            }

            override fun onTabReselected(tab: TabLayout.Tab) { }
        })

        /* viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
             override fun onPageScrollStateChanged(state: Int) {
             }
             override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
             }
             override fun onPageSelected(position: Int) {
             }
         })*/
    }

    override fun onBackPressed() {
        val delay = 500
        when {
            System.currentTimeMillis() > backKeyPressedTime + delay -> {
                backKeyPressedTime = System.currentTimeMillis()
                return
            }
            System.currentTimeMillis() <= backKeyPressedTime + delay -> {
                this.finish()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        onRestart()
    }

    inner class PagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int): Fragment {

            return when (position) {
                0 ->
                    TestFragment()
                1 ->
                    TestFragment()
                2 ->
                    TestFragment()
                3 ->
                    TestFragment()
                4 ->
                    TestFragment()
                else ->
                    TestFragment()
            }
        }

        override fun getCount(): Int = 5
    }
}