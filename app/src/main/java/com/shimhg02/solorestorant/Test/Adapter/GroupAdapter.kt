package com.shimhg02.solorestorant.Test.Adapter


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Data.GroupData
import com.shimhg02.solorestorant.Test.Data.GroupJoinData
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Story.StoryActivity
import com.shimhg02.solorestorant.ui.Activity.Term.TermActivity
import kotlinx.android.synthetic.main.item_group.view.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupAdapter(val testDataList:ArrayList<GroupData>): RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return testDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(v, testDataList)
    }

    override fun onBindViewHolder(holder: GroupAdapter.ViewHolder, position: Int) {
        holder.bindItems(testDataList[position])
    }


    class ViewHolder(view: View, var testDataList:ArrayList<GroupData>): RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bindItems(data : GroupData){
            val PREFERENCE = "com.shimhg02.honbab"
            val pref = itemView!!.context.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
            itemView.name_tv.text = data.groupName
            itemView.location_tv.text = data.vicinity
            itemView.time_tv.text = data.time
            itemView.people_tv.text = data.users.size.toString() + "/" + data.maximum.toString()
            if(data.isAdult){
                Glide.with(itemView.context).load("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Icon-not-under18.svg/1200px-Icon-not-under18.svg.png").into(itemView.image_is19)
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
                dialog.setMessage("솰라솰라 우흥우흥 부엉부엉 포켓포켓 횡령횡")
                dialog.setIcon(R.mipmap.ic_launcher)

                var dialog_listener = object: DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when(which){
                            DialogInterface.BUTTON_POSITIVE ->{
                                Client.retrofitService.joinGroup(pref.getString("token","").toString(), data.groupUUID).enqueue(object :
                                    Callback<GroupJoinData> {
                                    override fun onResponse(call: Call<GroupJoinData>?, response: Response<GroupJoinData>?) {
                                        when (response!!.code()) {
                                            200 -> {
                                                var dialog = AlertDialog.Builder(itemView.context)
                                                dialog.setTitle("그룹가입 성공")
                                                dialog.setMessage("채팅방이 생성되었습니다.\n즐거운 시간 보내세요!")
                                                dialog.setIcon(R.mipmap.ic_launcher)
                                                dialog.show()
                                            }
                                            201 -> {
                                                var dialog = AlertDialog.Builder(itemView.context)
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

private fun Any.putExtra(statusResourcesKey: String, testDataAsset: MutableList<String>) {

}
