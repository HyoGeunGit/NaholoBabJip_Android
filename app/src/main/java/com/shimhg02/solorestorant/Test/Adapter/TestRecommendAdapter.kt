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
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.ui.Activity.Story.StoryTest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recomendfood.view.*
import kotlinx.android.synthetic.main.item_story.view.*
import kotlinx.android.synthetic.main.item_story.view.image_story
import kotlinx.android.synthetic.main.item_story.view.name_tv
import java.lang.reflect.Array



class TestRecommendAdapter(val testDataList:ArrayList<TestRecommendData>): RecyclerView.Adapter<TestRecommendAdapter.ViewHolder>() {

    //아이템의 갯수를 설정해줍니다 (저 안의 숫자는 보통 .size로 지정해줍니다.)
    override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestRecommendAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recomendfood, parent, false)
        return ViewHolder(v, testDataList)
    }

    override fun onBindViewHolder(holder: TestRecommendAdapter.ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }
    class ViewHolder(view: View, var testDataList:ArrayList<TestRecommendData>): RecyclerView.ViewHolder(view) {
        fun bindItems(data : TestRecommendData){
            val testDataListOn= arrayOf(
                data.image
            )
            System.out.println("LOGD1: " + testDataListOn)
            Picasso.get().load(data.image).into(itemView.image_story)
            itemView.name_tv.setText(data.name)
            itemView.sub_tv.setText(data.subText)
        }
    }
}
