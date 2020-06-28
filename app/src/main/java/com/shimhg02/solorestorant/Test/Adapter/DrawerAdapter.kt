package com.shimhg02.solorestorant.Test.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Data.DrawerData
import java.util.ArrayList


internal class DrawerAdapter(private val dataList: ArrayList<DrawerData>): RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_drawer_friend, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(dataList[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(data: DrawerData){
            //각각의 아이템 클릭할때의 이벤트를 띄워줍니다.
        }

    }


}
