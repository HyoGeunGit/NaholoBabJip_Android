package com.shimhg02.solorestorant.Test.Activity

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onebyone)
        try {
            mSocket = IO.socket("http://13.59.89.201:8001")
        } catch (e: URISyntaxException) {
            Log.e("OnebyoneActivity", e.reason)
        }

        mSocket.connect()

        mSocket.on("emitVip", emitVip)
        mSocket.on("emitSex", emitSex)
        mSocket.on("emitUuid", emitUuid)
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
}