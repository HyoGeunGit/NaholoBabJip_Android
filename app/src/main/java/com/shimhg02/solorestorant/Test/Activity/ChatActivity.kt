package com.shimhg02.solorestorant.Test.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Adapter.DrawerAdapter
import com.shimhg02.solorestorant.Test.Data.ChatDTO
import com.shimhg02.solorestorant.Test.Data.DrawerData
import com.shimhg02.solorestorant.utils.GpsUtil.GpsTracker
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_content_main.*
import java.io.IOException
import java.util.*


class ChatActivity:AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val itemss = java.util.ArrayList<DrawerData>()
    private var recyclerView: RecyclerView? = null
    private var adapterd: DrawerAdapter? = null
    var CHAT_NAME:String = ""
    var USER_NAME:String = ""
    var chat_view: ListView? = null
    var chat_send: Button? = null
    var chat_name: TextView? = null
    var onon: ImageView? = null
    var cameraas: ImageView? = null
    var Videoss: ImageView? = null
    var Batts: ImageView? = null
    val homes: ImageView? = null
    var onners: LinearLayout? = null
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference
    private val imm: InputMethodManager? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_content_main)
        keyboardEvent()

        // 위젯 ID 참조
        chat_view = findViewById<ListView>(R.id.chat_view)
        chat_send = findViewById<Button>(R.id.chat_sent)
        chat_name = findViewById<TextView>(R.id.chat_names)
        onon = findViewById<ImageView>(R.id.onon)
        onners = findViewById<LinearLayout>(R.id.onner)
        cameraas = findViewById<ImageView>(R.id.camera)
        Videoss = findViewById<ImageView>(R.id.video)
        val toolbar = home
        chat_view!!.divider = null
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val i = intArrayOf(0)
        onon!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                if (i[0] == 0)
                {
                    onners!!.visibility = View.VISIBLE
                    imm.hideSoftInputFromWindow(chat_edit!!.getWindowToken(),0);
                    i[0]++
                }
                else
                {
                    onners!!.visibility = View.GONE
                    i[0] = 0
                }
            }
        })
        Videoss!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                getWholeListViewItemsToBitmap()
            }
        })

        cameraas!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                var gpsTracker = GpsTracker(this@ChatActivity)
                val latitude: Double = gpsTracker.getLatitude()
                val longitude: Double = gpsTracker.getLongitude()
                val address: String = getCurrentAddress(latitude, longitude).toString()
                chat_edit.setText(address)
            }
        })
        var navHeader = nav_view.getHeaderView(0)
//        recyclerView = navHeader.findViewById(R.id.recyrecy)
//        recyclerView?.setHasFixedSize(true)
//        recyclerView?.layoutManager = LinearLayoutManager(this@ChatActivity, LinearLayout.VERTICAL, false)
//        recyclerView?.adapter =  com.si.tototalk.Adapter.DrawerAdapter(itemss)
//        adapterd = recyclerView?.adapter as DrawerAdapter?
//        Client.retrofitService.getGroupMember(intent.getStringExtra("chatName")).enqueue(object : Callback<ArrayList<DrawerData>> {
//            override fun onResponse(
//                call: Call<ArrayList<DrawerData>>?,
//                response: Response<ArrayList<DrawerData>>?
//            ) {
//                val repo = response!!.body()
//
//                when (response.code()) {
//                    200 -> {
//                        repo!!.indices.forEach {
//                            itemss += DrawerData(
//                                repo[it].name,
//                                repo[it].docID,
//                                repo[it].profileImg
//                            )
//                            recyclerView?.adapter?.notifyDataSetChanged()
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ArrayList<DrawerData>>?, t: Throwable?) {
//                Log.v("C2cTest", "fail!!")
//            }
//        })
        val drawer = drawer_layout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.colorAccent)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        val intent = intent
//        CHAT_NAME = intent.getStringExtra("chatName")
//        USER_NAME = intent.getStringExtra("userName")
        CHAT_NAME = "TEST"
        USER_NAME = "heyman"
//        chat_name!!.text = intent.getStringExtra("chatRoomName")
        // 채팅 방 입장
        chat_name!!.text = "TESTroom"
        openChat(CHAT_NAME)
        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_send!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                if (chat_edit!!.text.toString().equals(""))
                    return
                val chat = ChatDTO(USER_NAME, chat_edit!!.text.toString()) //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat) // 데이터 푸쉬
                chat_edit!!.setText("") //입력창 초기화
            }
        })
    }
    override fun onNavigationItemSelected(item:MenuItem):Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        val drawer = drawer_layout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
    private fun addMessage(dataSnapshot:DataSnapshot, adapter:ArrayAdapter<String>) {
        val chatDTO = dataSnapshot.getValue(ChatDTO::class.java)
        adapter.add(chatDTO!!.message)
    }
    private fun removeMessage(dataSnapshot:DataSnapshot, adapter:ArrayAdapter<String>) {
        val chatDTO = dataSnapshot.getValue(ChatDTO::class.java)
        if (chatDTO!!.message === "새로운 채팅방이 생성되었습니다.")
        {
            adapter.remove(chatDTO!!.message)
        }
    }
    private fun openChat(chatName:String) {
        // 리스트 어댑터 생성 및 세팅
        val adapter = ArrayAdapter<String>(this, R.layout.item_chatchat, android.R.id.text1)
        chat_view?.setAdapter(adapter)
        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(object:
            ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                addMessage(p0, adapter)
                Log.e("LOG", "s:" + p1)
            }
            override fun onChildRemoved(dataSnapshot:DataSnapshot) {
                removeMessage(dataSnapshot, adapter)
            }
            override fun onCancelled(databaseError:DatabaseError) {
            }
        })
    }

    private fun keyboardEvent(){
        chat_edit!!.setOnClickListener {
            onners!!.visibility = View.GONE
        }
    }

    fun getCurrentAddress(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        addresses = try {
            geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.size == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        }
        val address: Address = addresses[0]
        return address.getAddressLine(0).toString().toString() + "\n"
    }
    fun getWholeListViewItemsToBitmap(): Bitmap? {
        val listview: ListView? = chat_view
        val adapter = listview!!.adapter
        val itemscount = adapter.count
        var allitemsheight = 0
        val bmps: List<Bitmap> = ArrayList()
        for (i in 0 until itemscount) {
            val childView = adapter.getView(i, null, listview)
            childView.measure(
                View.MeasureSpec.makeMeasureSpec(listview.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            childView.layout(0, 0, childView.measuredWidth, childView.measuredHeight)
            childView.isDrawingCacheEnabled = true
            childView.buildDrawingCache()
            bmps.apply {childView.drawingCache }
            allitemsheight += childView.measuredHeight
        }
        val bigbitmap = Bitmap.createBitmap(
            listview.measuredWidth,
            allitemsheight,
            Bitmap.Config.ARGB_8888
        )
        val bigcanvas = Canvas(bigbitmap)
        val paint = Paint()
        var iHeight = 0
        for (i in 0 until bmps.size) {
            var bmp: Bitmap? = bmps[i]
            bigcanvas.drawBitmap(bmp!!, 0f, iHeight.toFloat(), paint)
            iHeight += bmp.height
            bmp.recycle()
            bmp = null
        }
        return bigbitmap
    }
}