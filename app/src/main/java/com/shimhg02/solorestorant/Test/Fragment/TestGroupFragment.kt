package com.shimhg02.solorestorant.Test.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.shimhg02.solorestorant.R
import kotlinx.android.synthetic.main.fragment_group.view.*


class TestGroupFragment : Fragment() { //프레그먼트를 띄우기 위해 주로 사용합니다.

    private var isFabOpen = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)


        view.fab_main.setOnClickListener {
            toggleFab();
        }
        view.fab_sub1.setOnClickListener {
            toggleFab();
        }
        view.fab_sub2.setOnClickListener {
            toggleFab();
        }
        return view
    }
    private fun toggleFab() {
        var fab_open = AnimationUtils.loadAnimation(activity!!.baseContext.applicationContext, R.anim.fab_open)
        var fab_close = AnimationUtils.loadAnimation(activity!!.baseContext.applicationContext, R.anim.fab_close)
        if (isFabOpen) {
            view!!.fab_main.setImageResource(R.drawable.ic_plus)
            view!!.fab_sub1.startAnimation(fab_close)
            view!!.fab_sub2.startAnimation(fab_close)
            view!!.fab_sub1.setClickable(false)
            view!!.fab_sub2.setClickable(false)
            isFabOpen = false
        } else {
            view!!.fab_main.setImageResource(R.drawable.ic_close)
            view!!.fab_sub1.startAnimation(fab_open)
            view!!.fab_sub2.startAnimation(fab_open)
            view!!.fab_sub1.setClickable(true)
            view!!.fab_sub2.setClickable(true)
            isFabOpen = true
        }
    }
}