package com.shimhg02.solorestorant.Adapter.Chat.ChatList


import android.annotation.SuppressLint
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.ui.Activity.Chat.ChatActivity
import com.shimhg02.solorestorant.network.Data.ChatData.ChatListData
import kotlinx.android.synthetic.main.item_chat_view.view.*

/**
 * @description 그룹 어댑터
 */

class ChatListAdapter(val testDataList:ArrayList<ChatListData>): RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_view, parent, false)
        return ViewHolder(
            v,
            testDataList
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }


    class ViewHolder(view: View, var testDataList:ArrayList<ChatListData>): RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bindItems(data : ChatListData){
            val PREFERENCE = "com.shimhg02.honbab"
            val pref = itemView.context.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
            itemView.name_tv.text = data.groupName
            itemView.final_tv.text = data.lastMessage
            itemView.time_tv.text = converteTimestamp(data.timeStamp)
            val intent = Intent(itemView.context, ChatActivity::class.java) //TODO: StorieActivity 포맷 변경
            System.out.println("LOGD GROUPUUID: ${data.groupUUID}")
            intent.putExtra("chatUUID", data.groupUUID.toString())
            intent.putExtra("userName", pref.getString("nick",""))
            intent.putExtra("chatName", data.groupName)
            itemView.setOnClickListener {
                itemView.context.startActivity(intent)
            }
        }
    }
}

fun converteTimestamp(mileSegundos: String): CharSequence? {
    return DateUtils.getRelativeTimeSpanString(
        mileSegundos.toLong(),
        System.currentTimeMillis(),
        DateUtils.SECOND_IN_MILLIS
    )
}
