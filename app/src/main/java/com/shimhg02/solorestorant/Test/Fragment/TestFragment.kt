package com.shimhg02.solorestorant.Test.Fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Adapter.TestAdapter
import com.shimhg02.solorestorant.Test.Adapter.TestData


class TestFragment : Fragment() { //프레그먼트를 띄우기 위해 주로 사용합니다.
    private var recyclerView: RecyclerView? = null
    private var adapterd: TestAdapter? = null
    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val testDataList = arrayListOf(
            TestData("심효근", "https://i.pinimg.com/originals/fc/c2/de/fcc2ded6298590d814a8e5c349467f63.jpg"),
            TestData("강은서", "https://steamuserimages-a.akamaihd.net/ugc/781851765953128487/1BF4AB9F9D350192D88DA1B5422A05D908E2B54B/"),
            TestData("박태욱", "https://cdn.class101.net/images/c854f359-2b91-48f7-9ec9-694931f45fec/412xauto"),
            TestData("이유진", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSLji8PMdMmR5034_w0fIKumzLFgCl5fuHrJPonCg5O9ZHnsfqg&usqp=CAU"),
            TestData("이민혁", "https://lh3.googleusercontent.com/proxy/qBjp0UbVFfArGEBTr_tXnwar4UxIlVWn1laTjb9Liot9FHuY7JFZabCMKKVPcbRseGUQBiR0HLtuu1AOmqr5js5Tcua_WNW_ijXett_BJG1svWhQ4lIXBI8Rx88RA3IOMZQaxuqOkbAdepAA2iL6uIkW7je02lbWiSQR0kZfUHeO6wA"),
            TestData("임수민", "https://i.pinimg.com/originals/3a/97/b1/3a97b1ff7ba1338dda57533416a6d08f.png"),
            TestData("박종훈", "https://pbs.twimg.com/media/ERo04MOU0AATugV.jpg")
        )
        val view = inflater.inflate(R.layout.fragment_teststory, container, false)
        recyclerView = view!!.findViewById(R.id.testStoryView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        recyclerView?.adapter =  TestAdapter(testDataList)
        adapterd = recyclerView?.adapter as TestAdapter?
        return view
    }
}