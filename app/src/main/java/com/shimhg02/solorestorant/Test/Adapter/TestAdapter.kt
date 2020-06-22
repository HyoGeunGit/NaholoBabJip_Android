package com.shimhg02.solorestorant.Test.Adapter


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.rahuljanagouda.statusstories.StatusStoriesActivity
import com.rahuljanagouda.statusstories.glideProgressBar.OkHttpProgressGlideModule
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.ui.Activity.Story.StoryTest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_story.view.*
import java.lang.reflect.Array



class TestAdapter(val testDataList:ArrayList<TestData>): RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    //아이템의 갯수를 설정해줍니다 (저 안의 숫자는 보통 .size로 지정해줍니다.)
    override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewHolder(v, testDataList)
    }

    override fun onBindViewHolder(holder: TestAdapter.ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }
    class ViewHolder(view: View, var testDataList:ArrayList<TestData>): RecyclerView.ViewHolder(view) {
        fun bindItems(data : TestData){
            val testDataListOn= arrayOf(
                data.image
            )
            System.out.println("LOGD1: " + testDataListOn)
            val a = Intent(itemView.context, StatusStoriesActivity::class.java) //TODO: StatusStoriesActivitiy 포맷 변경
            a.putExtra(StatusStoriesActivity.STATUS_RESOURCES_KEY, testDataListOn)
            a.putExtra(StatusStoriesActivity.STATUS_DURATION_KEY, 3000L)
            a.putExtra(StatusStoriesActivity.IS_IMMERSIVE_KEY, true)
            a.putExtra(StatusStoriesActivity.IS_CACHING_ENABLED_KEY, true)
            a.putExtra(StatusStoriesActivity.IS_TEXT_PROGRESS_ENABLED_KEY, true)
            Picasso.get().load(data.image).into(itemView.image_story)
            itemView.name_tv.setText(data.name)
            itemView.setOnClickListener {
                itemView.context.startActivity(a)
            }
        }
    }
}
