package com.shimhg02.solorestorant.Adapter.Story


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.StoryData.StoryData
import com.shimhg02.solorestorant.network.Data.StoryData.StoryDataSubListItem
import com.shimhg02.solorestorant.ui.Activity.Story.StoryActivity
import kotlinx.android.synthetic.main.item_story.view.*
import retrofit2.Call
import java.util.ArrayList

/**
 * @description 스토리 어댑터
 */

class StoryAdapter(val testDataList: ArrayList<StoryData>): RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

     override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewHolder(
            v, testDataList
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }


    class ViewHolder(view: View, var testDataList:  ArrayList<StoryData>): RecyclerView.ViewHolder(view) {
        fun bindItems(data : StoryData){
            System.out.println("LOGD TEST Adapter" + data.userName)
            val testDataAsset = arrayOf(
                data.imgUrl
            )
            System.out.println("LOGD1: " + testDataAsset)
            val a = Intent(itemView.context, StoryActivity::class.java) //TODO: StorieActivity 포맷 변경
//            a.putExtra(StoryActivity.STATUS_RESOURCES_KEY,testDataAsset )
//            a.putExtra(StoryActivity.STATUS_DURATION_KEY, 3000L)
//            a.putExtra(StoryActivity.STATUS_DURATION_KEY, 3000L)
//            a.putExtra(StoryActivity.IS_IMMERSIVE_KEY, true)
//            a.putExtra(StoryActivity.IS_CACHING_ENABLED_KEY, true)
//            a.putExtra(StoryActivity.STATUS_WRITER_KEY, data[data.size].userName)
//            a.putExtra(StoryActivity.STATUS_PROFILE_KEY, data[data.size].userProfileImgUrl)
//            a.putExtra(StoryActivity.IS_TEXT_PROGRESS_ENABLED_KEY, true)
            Glide.with(itemView.context).load(data.imgUrl).into(itemView.image_story)
            System.out.println("LOGD TEST: " + data.imgUrl)
            itemView.name_tv.setText(data.userName)
            itemView.setOnClickListener {
                itemView.context.startActivity(a)
            }
        }

    }
}

private fun Any.putExtra(statusResourcesKey: String, testDataAsset: MutableList<String>) {

}
