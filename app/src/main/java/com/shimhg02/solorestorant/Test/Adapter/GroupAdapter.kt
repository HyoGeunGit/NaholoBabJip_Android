package com.shimhg02.solorestorant.Test.Adapter


import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Data.GroupData
import com.shimhg02.solorestorant.Test.Data.GroupJoinData
import com.shimhg02.solorestorant.network.Data.LogIn
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Main.MainActivity
import kotlinx.android.synthetic.main.item_group.view.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupAdapter(val testDataList:ArrayList<GroupData>): RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(v, testDataList)
    }

    override fun onBindViewHolder(holder: GroupAdapter.ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }


    class ViewHolder(view: View, var testDataList:ArrayList<GroupData>): RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bindItems(data : GroupData){
            val PREFERENCE = "com.shimhg02.honbab"
            val pref = itemView!!.context.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
            itemView.name_tv.text = data.groupName
            itemView.location_tv.text = data.vicinity
            itemView.time_tv.text = data.time
            itemView.people_tv.text = data.users.size.toString() + "/" + data.maximum.toString()
            if(data.isAdult){
                Glide.with(itemView.context).load("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Icon-not-under18.svg/1200px-Icon-not-under18.svg.png").into(itemView.image_is19)
            }
            else{
                itemView.image_is19.setImageResource(R.drawable.ic_plus)
            }
            itemView.setOnClickListener {
                Client.retrofitService.joinGroup(pref.getString("token","").toString(), data.groupUUID).enqueue(object :
                    Callback<GroupJoinData> {
                    override fun onResponse(call: Call<GroupJoinData>?, response: Response<GroupJoinData>?) {
                        when (response!!.code()) {
                            200 -> {
                               Toast.makeText(itemView.context, "가입성공", Toast.LENGTH_SHORT).show()
                            }
                            404-> {
                                Toast.makeText(itemView.context, "중복그룹", Toast.LENGTH_SHORT).show()
                            }
                            500 -> Toast.makeText(itemView.context, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<GroupJoinData>?, t: Throwable?) {

                    }
                })
            }
        }
    }
}

private fun Any.putExtra(statusResourcesKey: String, testDataAsset: MutableList<String>) {

}
