package com.shimhg02.solorestorant.Adapter.Group


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.GroupData.GroupData
import com.shimhg02.solorestorant.network.Data.GroupData.GroupJoinData
import com.shimhg02.solorestorant.network.Retrofit.Client
import kotlinx.android.synthetic.main.item_group.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @description 그룹 어댑터
 */

@Suppress("NAME_SHADOWING")
class GroupAdapter(val testDataList:ArrayList<GroupData>): RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(
            v,
            testDataList
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }


    class ViewHolder(view: View, var testDataList:ArrayList<GroupData>): RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bindItems(data : GroupData){
            val PREFERENCE = "com.shimhg02.honbab"
            itemView.context.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
            itemView.name_tv.text = data.groupName
            itemView.location_tv.text = data.vicinity
            itemView.time_tv.text = data.time
            itemView.people_tv.text = data.users    .size.toString() + "/" + data.maximum.toString()
            if(data.isAdult){
                Glide.with(itemView.context).load("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Icon-not-under18.svg/1200px-Icon-not-under18.svg.png").asBitmap().diskCacheStrategy(
                    DiskCacheStrategy.ALL).thumbnail(0.1f).into(itemView.image_is19)
            }
            else{
                itemView.image_is19.setImageResource(R.drawable.ic_plus)
            }
            itemView.setOnClickListener {

                val pref = itemView.context.getSharedPreferences(PREFERENCE,
                    AppCompatActivity.MODE_PRIVATE
                )
                val editor = pref.edit()
                var dialog = AlertDialog.Builder(itemView.context)
                dialog.setTitle("정말 이 그룹에 참여하시겠습니까?")
                dialog.setMessage("TEST:TEST")
                dialog.setIcon(R.mipmap.ic_launcher)
                var dialog_listener = object: DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when(which){
                            DialogInterface.BUTTON_POSITIVE ->{
                                Client.retrofitService.joinGroup(pref.getString("token","").toString(), data.groupUUID).enqueue(object :
                                    Callback<GroupJoinData> {
                                    override fun onResponse(call: Call<GroupJoinData>?, response: Response<GroupJoinData>?) {
                                        when (response?.code()) {
                                            200 -> {
                                                System.out.println("테스트 200 성공")
                                                Toast.makeText(itemView.context,"성공",Toast.LENGTH_SHORT).show()
                                                val dialog = AlertDialog.Builder(itemView.context)
                                                dialog.setTitle("그룹가입 성공")
                                                dialog.setMessage("채팅방이 생성되었습니다.\n즐거운 시간 보내세요!")
                                                dialog.setIcon(R.mipmap.ic_launcher)
                                                dialog.show()
                                            }
                                            201 -> {
                                                Toast.makeText(itemView.context,"성공",Toast.LENGTH_SHORT).show()
                                                val dialog = AlertDialog.Builder(itemView.context)
                                                dialog.setTitle("그룹가입 성공")
                                                dialog.setMessage("채팅방이 생성되었습니다.\n즐거운 시간 보내세요!")
                                                dialog.setIcon(R.mipmap.ic_launcher)
                                                dialog.show()
                                            }
                                            400-> {
                                                Toast.makeText(itemView.context, "중복그룹", Toast.LENGTH_SHORT).show()
                                            }
                                            500 -> Toast.makeText(itemView.context, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    override fun onFailure(call: Call<GroupJoinData>?, t: Throwable?) {

                                    }
                                })
                            }
                            DialogInterface.BUTTON_NEGATIVE ->{
                                editor.clear()
                            }
                        }
                    }
                }

                dialog.setPositiveButton("네",dialog_listener)
                dialog.setNegativeButton("아니오",dialog_listener)
                dialog.show()
            }
        }
    }
}