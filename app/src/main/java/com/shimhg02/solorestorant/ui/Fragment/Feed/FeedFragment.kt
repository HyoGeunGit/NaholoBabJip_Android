package com.shimhg02.solorestorant.ui.Fragment.Feed


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shimhg02.solorestorant.Adapter.Story.StoryAdapter
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Adapter.TestRecommendAdapter
import com.shimhg02.solorestorant.network.Data.RecommendData.RecommendData
import com.shimhg02.solorestorant.network.Data.StoryData.StoryData
import com.shimhg02.solorestorant.network.Retrofit.Client
import retrofit2.Call
import retrofit2.Response

/**
 * @notice Test 그룹
 * @description 피드 프레그먼트
 */

class FeedFragment : Fragment() { //프레그먼트를 띄우기 위해 주로 사용합니다.
    private var recyclerView: RecyclerView? = null
    private var recyclerView2: RecyclerView? = null
    private var recyclerView3: RecyclerView? = null
    private var adapterd2: TestRecommendAdapter? = null
    private var adapter: StoryAdapter? = null
    private val items = java.util.ArrayList<StoryData>()

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val PREFERENCE = "com.shimhg02.honbab"
        val pref = activity!!.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)


        val recommendTestDataList = arrayListOf( //테스트용 더미데이터2
            RecommendData(
                "람다람",
                "킹갓람다람",
                "https://s3.marpple.co/files/u_14345/2020/4/original/2144663c7991a33e33469e2d23a25591e1cd47e568e767c.png"
            ),
            RecommendData(
                "람다람",
                "람다람일러좋아",
                "https://cdn.class101.net/images/8062f865-c1ec-459c-b3f6-3d67a928cda7/1200x630"
            )
        )

        val recommendTestDataList2 = arrayListOf( //테스트용 더미데이터3
            RecommendData(
                "람다람",
                "람다람 넣을게~",
                "https://steamuserimages-a.akamaihd.net/ugc/781851765958068828/D4B37575DDB625FFAF9E6D81D5DB43F622D23E51/?imw=512&&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false"
            ),
            RecommendData(
                "람다람",
                "람다람 왜 울고있는거야?",
                "https://cdn.class101.net/images/0c204339-b159-45c9-bf28-1335bb48125f/1200xauto"
            )
        )

        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        recyclerView = view!!.findViewById(R.id.recycler_story)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        recyclerView?.adapter =  StoryAdapter(items)
        adapter = recyclerView!!.adapter as StoryAdapter?

        Client.retrofitService.getStory().enqueue(object :
            retrofit2.Callback<ArrayList<StoryData>> {
            override fun onResponse(call: Call<ArrayList<StoryData>>?, response: Response<ArrayList<StoryData>>?) {
                val repo = response!!.body()
                when (response.code()) {
                    200 -> {
                        repo!!.indices.forEach {
                            items += StoryData(
                                repo[it].alreadyWatch,
                                repo[it].createdAt,
                                repo[it].imgUrl,
                                repo[it].storyUUID,
                                repo[it].userName,
                                repo[it].imgUrl,
                                repo[it].userUUID
                            )
                            recyclerView!!.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<StoryData>>?, t: Throwable?) {
                Log.v("C2cTest", "fail!!")
            }
        })

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