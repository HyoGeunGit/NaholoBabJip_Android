package com.shimhg02.solorestorant.Test.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.LoginData.LogIn
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException

class OnebyoneActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"
    private lateinit var mSocket: Socket
    var isVip: Boolean = false
    var sex: Boolean = false
    lateinit var uuid: String
    lateinit var groupUUID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onebyone)
        try {
            mSocket = IO.socket("http://13.59.89.201:8001")
        } catch (e: URISyntaxException) {
            Log.e("OnebyoneActivity", e.reason)
        }
        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("emitVip", emitVip)
        mSocket.on("emitSex", emitSex)
        mSocket.on("emitUuid", emitUuid)
        mSocket.on("newGroup", newGroup)
    }

    val emitVip: Emitter.Listener = Emitter.Listener {
        mSocket.emit("isVip", isVip)
    }

    val emitSex: Emitter.Listener = Emitter.Listener {
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        sex = pref.getBoolean("sex", true)
        mSocket.emit("sex", sex)
    }

    val emitUuid: Emitter.Listener = Emitter.Listener {
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        uuid = pref.getString("uuid", "").toString()
        mSocket.emit("uuid", uuid)
    }

    val onConnect: Emitter.Listener = Emitter.Listener {
        //여기서 다시 "login" 이벤트를 서버쪽으로 username 과 함께 보냅니다.
        //서버 측에서는 이 username을 whoIsON Array 에 추가를 할 것입니다.

        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        uuid = pref.getString("uuid", "").toString()
        sex = pref.getBoolean("sex", true)
        mSocket.emit("join onetoone", false,sex,uuid)
    }

    val newGroup: Emitter.Listener = Emitter.Listener {
        groupUUID = it[0].toString()
        val intent = Intent(this@OnebyoneActivity, ChatActivity::class.java)
        intent.putExtra("chatUUID", groupUUID)
        startActivity(intent)
    }
}