@file:Suppress("DEPRECATION")

package com.shimhg02.solorestorant.ui.Activity.Chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.material.navigation.NavigationView
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.shimhg02.solorestorant.Adapter.Chat.ChatFirebaseAdapter
import com.shimhg02.solorestorant.Adapter.Chat.ClickListenerChatFirebase
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.utils.FireBase.FirebaseUtil
import com.shimhg02.solorestorant.network.Data.ChatData.ChatModel
import com.shimhg02.solorestorant.network.Data.ChatData.ImageFileModel.FileModel
import com.shimhg02.solorestorant.network.Data.ChatData.LocationFileModel.MapModel
import com.shimhg02.solorestorant.network.Data.ChatData.UserModel
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_chat.*
import java.io.File
import java.util.*

/**
 * @description 채팅 Activity
 */
class ChatActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener, ClickListenerChatFirebase {
    var CHAT_REFERENCE: String? = null

    //Firebase and GoogleApiClient
    private val mFirebaseAuth: FirebaseAuth? = null
    private val mFirebaseUser: FirebaseUser? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mFirebaseDatabaseReference: DatabaseReference? = null
    var storage = FirebaseStorage.getInstance()
    var PREFERENCE = "com.shimhg02.honbab"
    var mDatabases: FirebaseDatabase? = null
    var dataRefs: DatabaseReference? = null

    //CLass Model
    private var userModel: UserModel? = null

    //Views UI
    private var rvListMessage: RecyclerView? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var btSendMessage: ImageView? = null
    private var btEmoji: ImageView? = null
    private var edMessage: EmojiconEditText? = null
    private var contentRoot: View? = null
    private var emojIcon: EmojIconActions? = null

    //File
    private var filePathImageCamera: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_chat)
        val pref =
            getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = pref.edit()
        CHAT_REFERENCE = intent!!.getStringExtra("chatUUID")
        editor.putString("FCMUUID", CHAT_REFERENCE)
        editor.apply()
        openDrawer()
        println("FCMUUID: " + pref.getString("FCMUUID", ""))
        println("CHAT LOG TEST :" + intent!!.getStringExtra("chatUUID"))
        chatRoomName.text = intent.getStringExtra("chatName")
        if (!FirebaseUtil.verificaConexao(this)) {
            FirebaseUtil.initToast(this, "인터넷 연결이 불안정합니다.")
            finish()
        } else {
            bindViews()
            verificaUsuarioLogado()
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build()
        }
        val b1: ImageView = findViewById<View>(R.id.camera) as ImageView //버튼 변수 선언 //xml에서 정의한 버튼을 변수에 대입
        val b2: ImageView = findViewById<View>(R.id.gallery) as ImageView //버튼 변수 선언 //xml에서 정의한 버튼을 변수에 대입
        val b3: ImageView = findViewById<View>(R.id.location) as ImageView //버튼 변수 선언 //xml에서 정의한 버튼을 변수에 대입
        val b0: ImageView = findViewById<View>(R.id.button_menuon) as ImageView //버튼 변수 선언 //xml에서 정의한 버튼을 변수에 대입
        val p0: LinearLayout = findViewById<View>(R.id.ononon) as LinearLayout //xml에서 정의한 버튼을 변수에 대입
        val a = intArrayOf(0)
        b0.setOnClickListener { //처리하는 무명클래스
            if (a[0] == 0) {
                p0.visibility = View.VISIBLE
                a[0] = 1
            } else {
                p0.visibility = View.GONE
                a[0] = 0
            }
        }
        b1.setOnClickListener { //처리하는 무명클래스
            verifyStoragePermissions()
            photoCameraIntent()
        }
        b2.setOnClickListener { //처리하는 무명클래스
            photoGalleryIntent()
        }
        b3.setOnClickListener { //처리하는 무명클래스
            callPlacePicker()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val storageRef =
            storage.getReferenceFromUrl(FirebaseUtil.URL_STORAGE_REFERENCE)
                .child(FirebaseUtil.FOLDER_STORAGE_IMG)
        if (requestCode == IMAGE_GALLERY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val selectedImageUri = data!!.data
                if (selectedImageUri != null) {
                    sendFileFirebase(storageRef, selectedImageUri)
                } else {
                    //URI IS NULL
                }
            }
        } else if (requestCode == IMAGE_CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (filePathImageCamera != null && filePathImageCamera!!.exists()) {
                    val imageCameraRef =
                        storageRef.child(filePathImageCamera!!.name + "_camera")
                    sendFileFirebase(imageCameraRef, filePathImageCamera!!)
                } else {
                    //IS NULL
                }
            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                if (place != null) {
                    val latLng = place.latLng
                    val mapModel =
                        MapModel(latLng.latitude.toString() + "", latLng.longitude.toString() + "")
                    val chatModel = ChatModel(
                        userModel,
                        Calendar.getInstance().time.time.toString() + "",
                        mapModel
                    )
                    mFirebaseDatabaseReference!!.child(CHAT_REFERENCE!!).push().setValue(chatModel)
                } else {
                    //PLACE IS NULL
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_chat, menu)
        return true
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed:$connectionResult")
        FirebaseUtil.initToast(this, "Google Play Services error.")
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonMessage -> sendMessageFirebase()
        }
    }

    override fun clickImageChat(
        view: View?,
        position: Int,
        nameUser: String?,
        urlPhotoUser: String?,
        urlPhotoClick: String?
    ) {
//        Intent intent = new Intent(this,FullScreenImageActivity.class);
//        intent.putExtra("nameUser",nameUser);
//        intent.putExtra("urlPhotoUser",urlPhotoUser);
//        intent.putExtra("urlPhotoClick",urlPhotoClick);
//        startActivity(intent);
    }

    override fun clickImageMapChat(
        view: View?,
        position: Int,
        latitude: String?,
        longitude: String?
    ) {
        val uri =
            String.format("geo:%s,%s?z=17&q=%s,%s", latitude, longitude, latitude, longitude)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private fun sendFileFirebase(
        storageReference: StorageReference?,
        file: Uri
    ) {
        if (storageReference != null) {
            val name =
                DateFormat.format("yyyy-MM-dd_hhmmss", Date())
                    .toString()
            val imageGalleryRef = storageReference.child(name + "_gallery")
            val uploadTask = imageGalleryRef.putFile(file)
            uploadTask.addOnFailureListener { e ->
                Log.e(
                    TAG,
                    "onFailure sendFileFirebase " + e.message
                )
            }
                .addOnSuccessListener { taskSnapshot ->
                    Log.i(TAG, "onSuccess sendFileFirebase")
                    val downloadUrl =
                        taskSnapshot.metadata!!.reference!!.downloadUrl
                    val fileModel = FileModel("img", downloadUrl.toString(), name, "")
                    val chatModel = ChatModel(
                        userModel,
                        "",
                        Calendar.getInstance().time.time.toString() + "",
                        fileModel
                    )
                    mFirebaseDatabaseReference!!.child(CHAT_REFERENCE!!).push().setValue(chatModel)
                }
        } else {
        }
    }

    private fun sendFileFirebase(
        storageReference: StorageReference?,
        file: File
    ) {
        if (storageReference != null) {
            val photoURI = FileProvider.getUriForFile(
                this@ChatActivity,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
            val uploadTask = storageReference.putFile(photoURI)
            uploadTask.addOnFailureListener { e ->
                Log.e(
                    TAG,
                    "onFailure sendFileFirebase " + e.message
                )
            }
                .addOnSuccessListener { taskSnapshot ->
                    Log.i(TAG, "onSuccess sendFileFirebase")
                    val downloadUrl =
                        taskSnapshot.metadata!!.reference!!.downloadUrl
                    val fileModel = FileModel(
                        "img",
                        downloadUrl.toString(),
                        file.name,
                        file.length().toString() + ""
                    )
                    val chatModel = ChatModel(
                        userModel,
                        "",
                        Calendar.getInstance().time.time.toString() + "",
                        fileModel
                    )
                    mFirebaseDatabaseReference!!.child(CHAT_REFERENCE!!).push().setValue(chatModel)
                }
        } else {
        }
    }

    fun callPlacePicker() {
        val builder = PlacePicker.IntentBuilder()
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun photoCameraIntent() {
        val nomeFoto =
            DateFormat.format("yyyy-MM-dd_hhmmss", Date()).toString()
        filePathImageCamera = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            nomeFoto + "camera.jpg"
        )
        val it = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoURI = FileProvider.getUriForFile(
            this@ChatActivity,
            BuildConfig.APPLICATION_ID + ".provider",
            filePathImageCamera!!
        )
        it.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(it, IMAGE_CAMERA_REQUEST)
    }

    private fun photoGalleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(R.string.select_picture_title)
            ), IMAGE_GALLERY_REQUEST
        )
    }

    private fun sendMessageFirebase() {
        val model = ChatModel(
            userModel,
            edMessage!!.text.toString(),
            Calendar.getInstance().time.time.toString() + "",
            null
        )
        mFirebaseDatabaseReference!!.child(CHAT_REFERENCE!!).push().setValue(model)
        mFirebaseDatabaseReference!!.child(CHAT_REFERENCE!!).child("welcome").ref.removeValue()
        edMessage!!.text = null
        intent = getIntent()
    }

    private fun lerMessagensFirebase() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        val firebaseAdapter = ChatFirebaseAdapter(
            mFirebaseDatabaseReference!!.child(CHAT_REFERENCE!!),
            userModel!!.name,
            this
        )
        firebaseAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(
                positionStart: Int,
                itemCount: Int
            ) {
                super.onItemRangeInserted(positionStart, itemCount)
                val friendlyMessageCount = firebaseAdapter.itemCount
                val lastVisiblePosition =
                    mLinearLayoutManager!!.findLastCompletelyVisibleItemPosition()
                if (lastVisiblePosition == -1 ||
                    positionStart >= friendlyMessageCount - 1 &&
                    lastVisiblePosition == positionStart - 1
                ) {
                    rvListMessage!!.scrollToPosition(positionStart)
                }
            }
        })
        rvListMessage!!.layoutManager = mLinearLayoutManager
        rvListMessage!!.adapter = firebaseAdapter
    }

    private fun verificaUsuarioLogado() {
        val pref =
            getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        intent = getIntent()
        userModel = UserModel(
            pref.getString("nick", ""),
            "https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png",
            "test",
            pref.getString("uuid", "")
        )
        lerMessagensFirebase()
    }

    /**
     * Vincular views com Java API
     */
    private fun bindViews() {
        contentRoot = findViewById(R.id.contentRoot)
        edMessage = findViewById<View>(R.id.editTextMessage) as EmojiconEditText
        btSendMessage =
            findViewById<View>(R.id.buttonMessage) as ImageView
        btSendMessage!!.setOnClickListener(this)
        btEmoji = findViewById<View>(R.id.buttonEmoji) as ImageView
        emojIcon = EmojIconActions(this, contentRoot, edMessage, btEmoji)
        emojIcon!!.ShowEmojIcon()
        rvListMessage = findViewById<View>(R.id.messageRecyclerView) as RecyclerView
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     */
    fun verifyStoragePermissions() {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            this@ChatActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this@ChatActivity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        } else {
            // we already have permission, lets go ahead and call camera intent
            photoCameraIntent()
        }
    }

    @SuppressLint("WrongConstant")
    fun openDrawer(){

        val toolbar = home
//        var recyclerView: RecyclerView? = null
//        var navHeader = nav_view.getHeaderView(0)
//        val itemss = java.util.ArrayList<DrawerData>()
//        var adapterd: DrawerAdapter? = null
//        recyclerView = navHeader.findViewById(R.id.recyrecy)
//        recyclerView?.setHasFixedSize(true)
//        recyclerView?.layoutManager = LinearLayoutManager(this@ChatActivity, LinearLayout.VERTICAL, false)
//        recyclerView?.adapter = DrawerAdapter(itemss)
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
    }

    override fun onNavigationItemSelected(item: MenuItem):Boolean {
        // Handle navigation view item clicks here.
        item.itemId
        val drawer = drawer_layout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE ->                 // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted
                    photoCameraIntent()
                }
        }
    }

    companion object {
        private const val IMAGE_GALLERY_REQUEST = 1
        private const val IMAGE_CAMERA_REQUEST = 2
        private const val PLACE_PICKER_REQUEST = 3
        val TAG = ChatActivity::class.java.simpleName

        // Storage Permissions
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}