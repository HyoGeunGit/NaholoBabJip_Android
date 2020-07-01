package com.shimhg02.solorestorant.Test.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.shimhg02.solorestorant.R

/**
 * @notice Test 그룹
 * @description 이벤트 메인화면 프레그먼트
 */

class TestEventMainFragment : Fragment() {


    private var mViewPager: ViewPager? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event_main, container, false)

        var fragmentManager = (activity)!!.supportFragmentManager
        mViewPager = view!!.findViewById(R.id.viewPager)
        mViewPager!!.adapter = PagerAdapter(fragmentManager)
        mViewPager!!.currentItem = 1

        val tabLayout = view!!.findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        // set icons
        tabLayout.getTabAt(0)!!.setText("이벤트") //탭 내부를 텍스트로 채우고 싶을때는 setText를 사용합니다.
        tabLayout.getTabAt(1)!!.setText("쿠폰")
        mViewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) = tab.select()
            override fun onTabUnselected(tab: TabLayout.Tab) { }
            override fun onTabReselected(tab: TabLayout.Tab) { }
        })
        return view
    }
    class PagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        //뷰페이져와 연결해주기 위해 사용됩니다.
        override fun getItem(position: Int): Fragment {

            return when (position) {
                0 ->
                    EventFragment()
                1 ->
                    EventFragment()
                else ->
                    EventFragment() //레이아웃 옮길때 에러시 이 프레그먼트 띄위기 필자(심효근)은 주로 마지막 프레그먼트를 띄웁니다.
            }
        }

        override fun getCount(): Int = 2
    }

}