package com.shimhg02.solorestorant.Test.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Activity.AddGroupActivity
import com.shimhg02.solorestorant.Test.Adapter.GroupAdapter
import com.shimhg02.solorestorant.Test.Data.GroupData
import com.shimhg02.solorestorant.network.Retrofit.Client
import kotlinx.android.synthetic.main.activity_addgroup.*
import kotlinx.android.synthetic.main.fragment_group.view.*
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TestGroupFragment : Fragment() { //프레그먼트를 띄우기 위해 주로 사용합니다.

    private var isFabOpen = false
    private var recyclerView: RecyclerView? = null
    private var adapter: GroupAdapter? = null
    private val items = java.util.ArrayList<GroupData>()
    val PREFERENCE = "com.shimhg02.honbab"

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)

        val pref = activity!!.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        recyclerView = view!!.findViewById(R.id.group_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView?.adapter = GroupAdapter(items)
        adapter = recyclerView!!.adapter as GroupAdapter?


        view.group_search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

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
                                    items += GroupData(repo[it].category,repo[it].groupName,repo[it].isAdult,repo[it].lat,repo[it].lng,repo[it].maximum,repo[it].time,repo[it].users,repo[it].vicinity, repo[it].groupUUID)
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

        Client.retrofitService.getGroup(1, pref.getString("token","").toString()).enqueue(object :
            retrofit2.Callback<ArrayList<GroupData>> {
            override fun onResponse(call: Call<ArrayList<GroupData>>?, response: Response<ArrayList<GroupData>>?) {
                val repo = response!!.body()
                when (response.code()) {
                    200 -> {
                        repo!!.indices.forEach {
                            items += GroupData(repo[it].category,repo[it].groupName,repo[it].isAdult,repo[it].lat,repo[it].lng,repo[it].maximum,repo[it].time,repo[it].users,repo[it].vicinity, repo[it].groupUUID)
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