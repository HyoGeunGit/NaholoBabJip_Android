package com.shimhg02.solorestorant.Test.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.RecommendData.RecommendData
import kotlinx.android.synthetic.main.item_recomendfood.view.*
import kotlinx.android.synthetic.main.item_story.view.image_story
import kotlinx.android.synthetic.main.item_story.view.name_tv


/**
 * @notice Test그룹
 * @description 리뷰 어댑터
 */

class TestRecommendAdapter(val dataList:ArrayList<RecommendData>): RecyclerView.Adapter<TestRecommendAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestRecommendAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recomendfood, parent, false)
        return ViewHolder(v, dataList)
    }

    override fun onBindViewHolder(holder: TestRecommendAdapter.ViewHolder, position: Int) {
        holder.bindItems(dataList[position])
    }
    class ViewHolder(view: View, var dataList:ArrayList<RecommendData>): RecyclerView.ViewHolder(view) {
        fun bindItems(data : RecommendData){
            val testDataListOn= arrayOf(
                data.image
            )
            System.out.println("LOGD test data: " + testDataListOn)
            Glide.with(itemView.context).load(data.image).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(itemView.image_story)
            itemView.name_tv.setText(data.name)
            itemView.sub_tv.setText(data.subText)
        }
    }
}
