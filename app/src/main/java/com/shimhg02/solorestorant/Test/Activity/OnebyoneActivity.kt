package com.shimhg02.solorestorant.Test.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.shimhg02.solorestorant.R
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException


class OnebyoneActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"
    private lateinit var mSocket: Socket
    val socketURI = "http://13.59.89.201:8001"
    var isVip: Boolean = false
    var sex: Boolean = false
    lateinit var uuid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onebyone)
        try {
            mSocket = IO.socket(socketURI)
        } catch (e: URISyntaxException) {
            Log.e("OnebyoneActivity", e.reason)
        }
        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
    }

    internal var onMatched: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
            val data = args[0] as JSONArray
            val groupUUID: String
            try {
                Log.d("asdasd", data.toString())
                groupUUID = data.getString(0)
                var UUID = groupUUID.split("\"")
                Log.d("new me",UUID[3])
                val intent = Intent(this@OnebyoneActivity, ChatActivity::class.java)
                intent.putExtra("chatUUID", UUID[3])
                intent.putExtra("userName", pref.getString("nick",""))
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                return@Runnable
            }
        })
    }

    val onConnect: Emitter.Listener = Emitter.Listener {
        //여기서 다시 "login" 이벤트를 서버쪽으로 username 과 함께 보냅니다.
        //서버 측에서는 이 username을 whoIsON Array 에 추가를 할 것입니다.

        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        uuid = pref.getString("uuid", "").toString()
        sex = pref.getBoolean("sex", true)
        mSocket.emit("join onetoone", false,sex,uuid)
        mSocket.on("matching success", onMatched)
    }
//
//    val newGroup: Emitter.Listener = Emitter.Listener {
//        groupUUID = it[0].toString()
//        val intent = Intent(this@OnebyoneActivity, ChatActivity::class.java)
//        intent.putExtra("chatUUID", groupUUID)
//        startActivity(intent)
//    }
}