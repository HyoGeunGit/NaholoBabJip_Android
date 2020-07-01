package com.shimhg02.solorestorant.Test.Adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Data.TestData
import com.shimhg02.solorestorant.ui.Activity.Story.StoryActivity
import kotlinx.android.synthetic.main.item_story.view.*

/**
 * @notice Test그룹
 * @description 스토리 어댑터
 */

class TestAdapter(val testDataList:ArrayList<TestData>): RecyclerView.Adapter<TestAdapter.ViewHolder>() {

     override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewHolder(v, testDataList)
    }

    override fun onBindViewHolder(holder: TestAdapter.ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }


    class ViewHolder(view: View, var testDataList:ArrayList<TestData>): RecyclerView.ViewHolder(view) {
        fun bindItems(data : TestData){

            val testDataAsset = arrayOf(
                data.image
            )
            System.out.println("LOGD1: " + testDataAsset)
            val a = Intent(itemView.context, StoryActivity::class.java) //TODO: StorieActivity 포맷 변경
            a.putExtra(StoryActivity.STATUS_RESOURCES_KEY,testDataAsset )
            a.putExtra(StoryActivity.STATUS_DURATION_KEY, 3000L)
            a.putExtra(StoryActivity.STATUS_DURATION_KEY, 3000L)
            a.putExtra(StoryActivity.IS_IMMERSIVE_KEY, true)
            a.putExtra(StoryActivity.IS_CACHING_ENABLED_KEY, true)
            a.putExtra(StoryActivity.STATUS_WRITER_KEY, data.name)
            a.putExtra(StoryActivity.STATUS_PROFILE_KEY, data.profileImage)
            a.putExtra(StoryActivity.IS_TEXT_PROGRESS_ENABLED_KEY, true)
            Glide.with(itemView.context).load(data.image).into(itemView.image_story)
            itemView.name_tv.setText(data.name)
            itemView.setOnClickListener {
                itemView.context.startActivity(a)
            }
        }

    }
}

private fun Any.putExtra(statusResourcesKey: String, testDataAsset: MutableList<String>) {

}
