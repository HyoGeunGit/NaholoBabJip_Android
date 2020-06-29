package com.shimhg02.solorestorant.Test.Fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shimhg02.solorestorant.R


class EventFragment : Fragment() { //프레그먼트를 띄우기 위해 주로 사용합니다.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_event, container, false)
    }
}