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
import com.shimhg02.solorestorant.Test.Data.TestData
import com.shimhg02.solorestorant.Test.Adapter.TestRecommendAdapter
import com.shimhg02.solorestorant.Test.Data.TestRecommendData


class TestFragment : Fragment() { //프레그먼트를 띄우기 위해 주로 사용합니다.
    private var recyclerView: RecyclerView? = null
    private var recyclerView2: RecyclerView? = null
    private var recyclerView3: RecyclerView? = null
    private var adapterd: TestAdapter? = null
    private var adapterd2: TestRecommendAdapter? = null

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val testDataList = arrayListOf( //테스트용 더미데이터1
            TestData(
                "심효근",
                "https://lh3.googleusercontent.com/p/AF1QipPngyeVDJBGlvxC2HEyES3cR7e93br1py-x2IMy=s1600-w1600-h1600",
                "https://lh3.googleusercontent.com/p/AF1QipPngyeVDJBGlvxC2HEyES3cR7e93br1py-x2IMy=s1600-w1600-h1600"
            ),
            TestData(
                "강은서",
                "https://steamuserimages-a.akamaihd.net/ugc/781851765953128487/1BF4AB9F9D350192D88DA1B5422A05D908E2B54B/",
                "https://pbs.twimg.com/profile_images/1182930100024594434/BRQu9Kpy_400x400.jpg"
            ),
            TestData(
                "박태욱",
                "https://cdn.class101.net/images/c854f359-2b91-48f7-9ec9-694931f45fec/412xauto",
                "https://cdn.class101.net/images/11f4e712-6132-4a19-b4d3-7712e4546672/1200xauto"
            ),
            TestData(
                "이유진",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSLji8PMdMmR5034_w0fIKumzLFgCl5fuHrJPonCg5O9ZHnsfqg&usqp=CAU",
                "https://i.pinimg.com/originals/f7/95/bd/f795bd9971dd4df0c8ddd14c3ac9a965.jpg"
            ),
            TestData(
                "이민혁",
                "https://lh3.googleusercontent.com/proxy/qBjp0UbVFfArGEBTr_tXnwar4UxIlVWn1laTjb9Liot9FHuY7JFZabCMKKVPcbRseGUQBiR0HLtuu1AOmqr5js5Tcua_WNW_ijXett_BJG1svWhQ4lIXBI8Rx88RA3IOMZQaxuqOkbAdepAA2iL6uIkW7je02lbWiSQR0kZfUHeO6wA",
                "https://i.pinimg.com/originals/4f/49/53/4f4953b636a8bb4377fe8ddf3f37bb5f.jpg"
            ),
            TestData(
                "임수민",
                "https://i.pinimg.com/originals/3a/97/b1/3a97b1ff7ba1338dda57533416a6d08f.png",
                "https://pbs.twimg.com/media/EKSPb2RU0AAr9g6.jpg"
            ),
            TestData(
                "박종훈",
                "https://pbs.twimg.com/media/ERo04MOU0AATugV.jpg",
                "https://i.pinimg.com/originals/49/72/d4/4972d4c65f3b1ebdeda26bd2b524ea33.jpg"
            )
        )

        val recommendTestDataList = arrayListOf( //테스트용 더미데이터2
            TestRecommendData(
                "람다람",
                "킹갓람다람",
                "https://s3.marpple.co/files/u_14345/2020/4/original/2144663c7991a33e33469e2d23a25591e1cd47e568e767c.png"
            ),
            TestRecommendData(
                "람다람",
                "람다람일러좋아",
                "https://cdn.class101.net/images/8062f865-c1ec-459c-b3f6-3d67a928cda7/1200x630"
            )
        )

        val recommendTestDataList2 = arrayListOf( //테스트용 더미데이터3
            TestRecommendData(
                "람다람",
                "람다람 넣을게~",
                "https://steamuserimages-a.akamaihd.net/ugc/781851765958068828/D4B37575DDB625FFAF9E6D81D5DB43F622D23E51/?imw=512&&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false"
            ),
            TestRecommendData(
                "람다람",
                "람다람 왜 울고있는거야?",
                "https://cdn.class101.net/images/0c204339-b159-45c9-bf28-1335bb48125f/1200xauto"
            )
        )

        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        recyclerView = view!!.findViewById(R.id.testStoryView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        recyclerView?.adapter =  TestAdapter(testDataList)
        recyclerView = view!!.findViewById(R.id.recommend_foodView)
        recyclerView?.setHasFixedSize(true)

        recyclerView2 = view!!.findViewById(R.id.recommend_foodView)
        recyclerView2?.setHasFixedSize(true)
        recyclerView2?.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView2?.adapter =  TestRecommendAdapter(recommendTestDataList)
        adapterd2 = recyclerView2?.adapter as TestRecommendAdapter?

        recyclerView3 = view!!.findViewById(R.id.recommend_foodView2)
        recyclerView3?.setHasFixedSize(true)
        recyclerView3?.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView3?.adapter =  TestRecommendAdapter(recommendTestDataList2)
        adapterd2 = recyclerView3?.adapter as TestRecommendAdapter?
        return view
    }
}