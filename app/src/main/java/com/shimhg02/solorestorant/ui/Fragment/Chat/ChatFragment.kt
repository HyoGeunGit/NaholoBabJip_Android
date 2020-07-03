package com.shimhg02.solorestorant.ui.Fragment.Chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.shimhg02.solorestorant.Adapter.Chat.ChatList.ChatListAdapter
import com.shimhg02.solorestorant.Adapter.Group.GroupAdapter
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.ChatData.ChatListData
import com.shimhg02.solorestorant.network.Data.GroupData.GroupData
import com.shimhg02.solorestorant.network.Retrofit.Client
import kotlinx.android.synthetic.main.fragment_chat.view.*
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import retrofit2.Response

/**
 * @notice Test 그룹
 * @description 그룹 프레그먼트
 */

class ChatFragment : Fragment() { //프레그먼트를 띄우기 위해 주로 사용합니다.

    private var isFabOpen = false
    private var recyclerView: ShimmerRecyclerView? = null
    private var adapter: ChatListAdapter? = null
    private val items = java.util.ArrayList<ChatListData>()
    lateinit var mAdView : AdView
    val PREFERENCE = "com.shimhg02.honbab"

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        MobileAds.initialize(view.context, "ca-app-pub-3940256099942544/6300978111")
        mAdView = view.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val pref = activity!!.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        recyclerView = view!!.findViewById(R.id.group_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.showShimmerAdapter()
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView?.adapter = ChatListAdapter(items)
        adapter = recyclerView!!.adapter as ChatListAdapter?

        Client.retrofitService.getChat( pref.getString("token","").toString()).enqueue(object :
            retrofit2.Callback<ArrayList<ChatListData>> {
            override fun onResponse(call: Call<ArrayList<ChatListData>>?, response: Response<ArrayList<ChatListData>>?) {
                val repo = response!!.body()
                when (response.code()) {
                    200 -> {
                        repo!!.indices.forEach {
                            items += ChatListData(
                                repo[it].groupName,
                                repo[it].groupUUID,
                                repo[it].isAdult,
                                repo[it].lastMessage,
                                repo[it].timeStamp
                            )
                            recyclerView!!.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<ChatListData>>?, t: Throwable?) {
                Log.v("C2cTest", "fail!!")
            }
        })

        return view
    }

}