package com.shimhg02.solorestorant.Test.Adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.ui.Activity.ImageViewer.ImageViewerActivity
import kotlinx.android.synthetic.main.item_food.view.*


/**
 * @notice Test그룹
 * @description 음식 이미지 어댑터
 */

class TestFoodAdapter(val testDataList:ArrayList<FoodImageRepo>): RecyclerView.Adapter<TestFoodAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return ViewHolder(v, testDataList)
    }

    override fun onBindViewHolder(holder: TestFoodAdapter.ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }

    class ViewHolder(view: View, var testDataList:ArrayList<FoodImageRepo>): RecyclerView.ViewHolder(view) {
        fun bindItems(data : FoodImageRepo){
            System.out.println("PHOTO: "+data.photo.toString())
            Glide.with(itemView.context).load(data.photo).into(itemView.image_food)
            itemView.setOnClickListener {
                val a = Intent(itemView.context, ImageViewerActivity::class.java) //TODO: StoryActivity 포맷 변경
                a.putExtra("imageView",data.photo)
                itemView.context.startActivity(a)
            }
        }
    }
}

private fun Any.putExtra(statusResourcesKey: String, testDataAsset: MutableList<String>) {

}
