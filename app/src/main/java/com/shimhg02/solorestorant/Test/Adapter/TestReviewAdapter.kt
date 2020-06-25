package com.shimhg02.solorestorant.Test.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shimhg02.solorestorant.R


class TestReviewAdapter(val testDataList:ArrayList<FoodReviewRepo>): RecyclerView.Adapter<TestReviewAdapter.ViewHolder>() {

    //아이템의 갯수를 설정해줍니다 (저 안의 숫자는 보통 .size로 지정해줍니다.)
    override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return ViewHolder(v, testDataList)
    }

    override fun onBindViewHolder(holder: TestReviewAdapter.ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }


    class ViewHolder(view: View, var testDataList:ArrayList<FoodReviewRepo>): RecyclerView.ViewHolder(view) {
        fun bindItems(data : FoodReviewRepo){

            itemView.setOnClickListener {
            }
        }

    }
}

private fun Any.putExtra(statusResourcesKey: String, testDataAsset: MutableList<String>) {

}
