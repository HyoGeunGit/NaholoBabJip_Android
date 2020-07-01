package com.shimhg02.solorestorant.Test.Activity


import android.annotation.SuppressLint
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Adapter.FoodImageRepo
import com.shimhg02.solorestorant.Test.Adapter.FoodReviewRepo
import com.shimhg02.solorestorant.Test.Adapter.TestFoodAdapter
import com.shimhg02.solorestorant.Test.Data.TestInfoData
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.utils.Bases.BaseActivity
import com.shimhg02.solorestorant.utils.Preference.SharedPref
import kotlinx.android.synthetic.main.activity_info_test.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @notice Test 그룹
 * @description 음식점 상세정보 보기 Activity
 */


class TestInfoActivity : BaseActivity() {
    private val itemss = java.util.ArrayList<FoodImageRepo>()
    private val items2 = java.util.ArrayList<FoodReviewRepo>()
    private var recyclerView: RecyclerView? = null
    private var recyclerView2: RecyclerView? = null
    private var adapterd: TestFoodAdapter? = null
    override var viewId: Int = R.layout.activity_info_test
    lateinit var mAdView : AdView

    @SuppressLint("WrongConstant")
    override fun onCreate() {
        SharedPref.openSharedPrep(this)
        recyclerView = findViewById(R.id.recyfood)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        recyclerView?.adapter =  TestFoodAdapter(itemss)
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111")
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        Client.retrofitService.getDetail(intent.getStringExtra("chekOn").toString()).enqueue(object :
            Callback<TestInfoData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<TestInfoData>?, response: Response<TestInfoData>?) {

                val repo = response!!.body()
                when (response!!.code()) {
                    200 -> {
                        System.out.println("LOGDD" + response.body()!!.photo)
                            tv_1.text = response.body()!!.name
                            tv_2.text = response.body()!!.vicinity
                            tv_3.text = response.body()!!.formatted_phone_number
                            tv_4.text = response.body()!!.openTime +"  "+ if(response.body()!!.openNow == true){"영업중"}else{"문닫음"}
                            response.body()!!.photo.indices.forEach {
                                itemss += FoodImageRepo(
                                    response.body()!!.photo[it]
                                )
                                recyclerView?.adapter?.notifyDataSetChanged()
                            }
                        System.out.println("LOGDD items" +  response.body()!!.reviews)
                    }
                    203-> {
                    }
                }
            }
            override fun onFailure(call: Call<TestInfoData>?, t: Throwable?) {

            }
        })
    }
}

private fun <T> Call<T>.enqueue(callback: Callback<TestInfoData>) {
}
