package com.shimhg02.solorestorant.Test.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shimhg02.solorestorant.R
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_onebyone.*
import org.json.JSONArray
import java.net.URISyntaxException


class OnebyoneActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"
    private lateinit var mSocket: Socket
    val socketURI = "http://13.59.89.201:8001"
    var isVip: Boolean = false
    var sex: Boolean = false
    lateinit var uuid: String
    var arrMessages: ArrayList<String> = ArrayList()
    var position = 0
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onebyone)
        setupLottie()
        setTextData()
        setTextAnimation()
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
            var sexes : String
            try {
                Log.d("asdasd", data.toString())
                var UUID = data.toString().split("\"")
                var datasex = UUID[10].split("}")
                System.out.println("GROUP: ${UUID[3]} NAME: ${UUID[7]} SEX: ${datasex[0]}")
                Log.d("new me",UUID[3])
                Toast.makeText(this@OnebyoneActivity,"축하드립니다 ${UUID[7]}님과 매칭되셨습니다. 상대 성별은 ${
                when(datasex[0].toString()) {
                    ":true" -> "남자"
                    ":false" -> "여자"
                    else -> "성별 불능"
                }
                } 입니다.",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@OnebyoneActivity, ChatActivity::class.java)
                intent.putExtra("chatUUID", UUID[3])
                intent.putExtra("userName", pref.getString("nick",""))
                startActivity(intent)
                mSocket.disconnect()
                mSocket.off(Socket.EVENT_DISCONNECT, onConnect)
                finish()
            } catch (e: Exception) {
                return@Runnable
            }
        })
    }
    override fun onStop() {
        super.onStop()
        mSocket.disconnect()
        mSocket.off()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off(Socket.EVENT_DISCONNECT, onMatched)
        mSocket.close()
        Log.d("SOCKET LIFECYCLE","DISCONNECT ONSTOP")
    }

    override fun onPause() {
        super.onPause()
        mSocket.disconnect()
        mSocket.off()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off(Socket.EVENT_DISCONNECT, onMatched)
        mSocket.close()
        Log.d("SOCKET LIFECYCLE","DISCONNECT ONPAUSE")
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off(Socket.EVENT_DISCONNECT, onMatched)
        mSocket.close()
        Log.d("SOCKET LIFECYCLE","DISCONNECT ONDESTROY")
    }

    val onConnect: Emitter.Listener = Emitter.Listener {
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        uuid = pref.getString("uuid", "").toString()
        sex = pref.getBoolean("sex", true)
        mSocket.emit("join onetoone", false,sex,uuid)
        mSocket.on("matching success", onMatched)
    }

    fun setupLottie(){
        animation_view.setAnimation("one_load.json")
        animation_view.playAnimation()
        animation_view.loop(true)
    }

    fun setTextData(){
        arrMessages.add("처음보는 사람에겐 예의를 지켜주세요!")
        arrMessages.add("두근두근 매칭... 과연 누구랑 될까요?")
        arrMessages.add("이성간매칭을 구매하시면 이성이랑 매칭이됩니다!")
        arrMessages.add("아 쓸내용이 없다")
        arrMessages.add("옹기잇")
    }

    fun setTextAnimation(){
        anime_text.animateText(arrMessages[position])
        position++
        var handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                handler.postDelayed(this, 5000)
                if (position >= arrMessages.size) position = 0
                anime_text.animateText(arrMessages[position])
                position++
            }
        }, 5000)
    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime: Long = tempTime - backPressedTime
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            mSocket.disconnect()
            mSocket.off()
            mSocket.off(Socket.EVENT_CONNECT, onConnect)
            mSocket.off(Socket.EVENT_DISCONNECT, onMatched)
            mSocket.close()
            Log.d("SOCKET","DISCONNECT CUT")
            finish()
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "한번 더 누르면 1대 1 매칭이 종료됩니다..", Toast.LENGTH_SHORT).show()
        }
    }

//
//    val newGroup: Emitter.Listener = Emitter.Listener {
//        groupUUID = it[0].toString()
//        val intent = Intent(this@OnebyoneActivity, ChatActivity::class.java)
//        intent.putExtra("chatUUID", groupUUID)
//        startActivity(intent)
//    }
}