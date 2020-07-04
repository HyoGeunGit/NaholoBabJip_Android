package com.shimhg02.solorestorant.ui.Fragment.Group

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.shimhg02.solorestorant.Adapter.Group.GroupAdapter
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Activity.EditNicknameActivity
import com.shimhg02.solorestorant.Test.Activity.OnebyoneActivity
import com.shimhg02.solorestorant.network.Data.GroupData.GroupData
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Group.AddGroupActivity
import com.shimhg02.solorestorant.ui.Activity.SignUp.SignUpInfoActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_group.view.*
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import retrofit2.Response

/**
 * @notice Test 그룹
 * @description 그룹 프레그먼트
 */

class GroupFragment : Fragment() { //프레그먼트를 띄우기 위해 주로 사용합니다.

    private var isFabOpen = false
    private var recyclerView: ShimmerRecyclerView? = null
    private var adapter: GroupAdapter? = null
    private val items = java.util.ArrayList<GroupData>()
    val PREFERENCE = "com.shimhg02.honbab"

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)

        val pref = activity!!.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        recyclerView = view!!.findViewById(R.id.group_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.showShimmerAdapter()
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView?.adapter = GroupAdapter(items)
        adapter = recyclerView!!.adapter as GroupAdapter?


        view.group_search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }


            /**
             * @description_function edittext 쿼리 읽기 함수
             */
            override fun onQueryTextChange(newText: String?): Boolean {
//                System.out.println("query test2: " + newText)
                items.clear()
                recyclerView!!.adapter?.notifyDataSetChanged()
                Client.retrofitService.searchGroup(pref.getString("token","").toString(), newText.toString()).enqueue(object :
                    retrofit2.Callback<ArrayList<GroupData>> {
                    override fun onResponse(call: Call<ArrayList<GroupData>>?, response: Response<ArrayList<GroupData>>?) {
                        val repo = response!!.body()
                        when (response.code()) {
                            200 -> {
                                repo!!.indices.forEach {
                                    items += GroupData(
                                        repo[it].category,
                                        repo[it].groupName,
                                        repo[it].isAdult,
                                        repo[it].lat,
                                        repo[it].lng,
                                        repo[it].maximum,
                                        repo[it].time,
                                        repo[it].users,
                                        repo[it].vicinity,
                                        repo[it].groupUUID
                                    )
                                    System.out.println("LOGDS GROUP" + items)
                                    recyclerView!!.adapter?.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<ArrayList<GroupData>>?, t: Throwable?) {
                        Log.v("C2cTest", "fail!!")
                    }
                })
                return false
            }
        })


        /**
         * @description_function Retrofit getGroup
         */
        Client.retrofitService.getGroup(1, pref.getString("token","").toString()).enqueue(object :
            retrofit2.Callback<ArrayList<GroupData>> {
            override fun onResponse(call: Call<ArrayList<GroupData>>?, response: Response<ArrayList<GroupData>>?) {
                val repo = response!!.body()
                when (response.code()) {
                    200 -> {
                        repo!!.indices.forEach {
                            items += GroupData(
                                repo[it].category,
                                repo[it].groupName,
                                repo[it].isAdult,
                                repo[it].lat,
                                repo[it].lng,
                                repo[it].maximum,
                                repo[it].time,
                                repo[it].users,
                                repo[it].vicinity,
                                repo[it].groupUUID
                            )
                            recyclerView!!.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<GroupData>>?, t: Throwable?) {
                Log.v("C2cTest", "fail!!")
            }
        })

        view.fab_main.setOnClickListener {
            toggleFab();
        }
        view.fab_sub1.setOnClickListener {
            startActivity<AddGroupActivity>()
            toggleFab();
        }
        view.fab_sub2.setOnClickListener {
            toggleFab();
        }
        view.fab_sub3.setOnClickListener {
            startActivity(Intent(activity, OnebyoneActivity::class.java))
        }
        return view
    }


    /**
     * @description_function FAB 토글링
     */
    private fun toggleFab() {
        var fab_open = AnimationUtils.loadAnimation(activity!!.baseContext.applicationContext, R.anim.fab_open)
        var fab_close = AnimationUtils.loadAnimation(activity!!.baseContext.applicationContext, R.anim.fab_close)
        if (isFabOpen) {
            view!!.fab_main.setImageResource(R.drawable.ic_plus)
            view!!.fab_sub1.startAnimation(fab_close)
            view!!.fab_sub2.startAnimation(fab_close)
            view!!.fab_sub3.startAnimation(fab_close)
            view!!.fabsub1.startAnimation(fab_close)
            view!!.fabsub2.startAnimation(fab_close)
            view!!.fabsub3.startAnimation(fab_close)
            view!!.blurView.visibility = View.GONE
            view!!.fab_sub1.setClickable(false)
            view!!.fab_sub2.setClickable(false)
            isFabOpen = false
        } else {
            view!!.fab_main.setImageResource(R.drawable.ic_close)
            view!!.fab_sub1.startAnimation(fab_open)
            view!!.fab_sub2.startAnimation(fab_open)
            view!!.fab_sub3.startAnimation(fab_open)
            view!!.fabsub1.startAnimation(fab_open)
            view!!.fabsub2.startAnimation(fab_open)
            view!!.fabsub3.startAnimation(fab_open)
            view!!.blurView.visibility = View.VISIBLE
            view!!.fab_sub1.setClickable(true)
            view!!.fab_sub2.setClickable(true)
            isFabOpen = true
        }
    }

}