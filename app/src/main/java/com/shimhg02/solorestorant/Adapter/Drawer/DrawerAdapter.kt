package com.shimhg02.solorestorant.Adapter.Drawer


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.DrawerData.DrawerData
import java.util.ArrayList


/**
 * @description 네비게이션 드로워 (채팅용) 어댑터
 */

internal class DrawerAdapter(private val dataList: ArrayList<DrawerData>): RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_drawer_friend, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(dataList[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(data: DrawerData){
            data.docID
            data.name
            data.profileImg
        }

    }


}
