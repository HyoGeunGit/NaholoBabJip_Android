package com.shimhg02.solorestorant.ui.Activity.CateFood

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Fragment.CateMapTestFragment
import com.shimhg02.solorestorant.Test.Fragment.MapTestFragment
import com.shimhg02.solorestorant.utils.Bases.BaseActivity
import kotlinx.android.synthetic.main.activity_catefood_recommend.*


class CateFoodActivity : BaseActivity() {

    override var viewId: Int = R.layout.activity_catefood_recommend
    private lateinit var toast: Toast
    private var backKeyPressedTime: Long = 200
    private var mViewPager: ViewPager? = null
    val PREFERENCE = "com.shimhg02.honbab"

    @SuppressLint("ShowToast")
    override fun onCreate() {
        mViewPager = findViewById(R.id.viewPager)
        mViewPager!!.adapter = PagerAdapter(supportFragmentManager)
        mViewPager!!.currentItem =  intent.getIntExtra("foodNum",0)

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
        // set icons
        tabLayout.getTabAt(0)!!.setText("혼밥추천") //탭 내부를 텍스트로 채우고 싶을때는 setText를 사용합니다.
        tabLayout.getTabAt(1)!!.setText("치킨")
        tabLayout.getTabAt(2)!!.setText("중식")
        tabLayout.getTabAt(3)!!.setText("일식")
        tabLayout.getTabAt(4)!!.setText("양식")
        tabLayout.getTabAt(5)!!.setText("분식")
        tabLayout.getTabAt(6)!!.setText("분식")
        tabLayout.getTabAt(7)!!.setText("분식")
        tabLayout.getTabAt(8)!!.setText("분식")
        tabLayout.getTabAt(9)!!.setText("분식")
        tabLayout.getTabAt(10)!!.setText("분식")
        tabLayout.getTabAt(11)!!.setText("분식")
        tabLayout.getTabAt(12)!!.setText("분식")
        mViewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab){
                val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                val editor = pref.edit()
                val pos = tab.position
                editor.putString("foodName", tabLayout.getTabAt(pos)!!.text.toString())
                editor.apply()
                food_category_text.text = tabLayout.getTabAt(pos)!!.text.toString()

            }

            override fun onTabUnselected(tab: TabLayout.Tab) { }

            override fun onTabReselected(tab: TabLayout.Tab) { }
        })
    }

    override fun onBackPressed() { //뒤로가기를 위해 사용합니다.
        val delay = 500
        when {
            System.currentTimeMillis() > backKeyPressedTime + delay -> {
                backKeyPressedTime = System.currentTimeMillis()
                toast.show()
                return
            }
            System.currentTimeMillis() <= backKeyPressedTime + delay -> {
                toast.cancel()
                this.finish()
            }
        }

    }

    inner class PagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
        //뷰페이져와 연결해주기 위해 사용됩니다.
        override fun getItem(position: Int): Fragment {

            return when (position) {
                in 0..12 ->
                    CateMapTestFragment()

                else ->
                    MapTestFragment()
            }
        }

        override fun getCount(): Int = 13
    }
}