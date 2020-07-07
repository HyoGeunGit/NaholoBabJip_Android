package com.shimhg02.solorestorant.ui.Activity.ImageEditor

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.ui.Activity.ImageEditor.EmojiBSFragment.EmojiListener
import com.shimhg02.solorestorant.ui.Activity.ImageEditor.StickerBSFragment.StickerListener
import com.shimhg02.solorestorant.ui.Activity.ImageEditor.base.BaseActivity
import com.shimhg02.solorestorant.ui.Activity.ImageEditor.filters.FilterListener
import com.shimhg02.solorestorant.ui.Activity.ImageEditor.filters.FilterViewAdapter
import com.shimhg02.solorestorant.ui.Activity.ImageEditor.tools.EditingToolsAdapter
import com.shimhg02.solorestorant.ui.Activity.ImageEditor.tools.EditingToolsAdapter.OnItemSelected
import com.shimhg02.solorestorant.ui.Activity.ImageEditor.tools.ToolType
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Main.MainActivity
import com.shimhg02.solorestorant.utils.Base64.encodeBitmapIntoBase64
import ja.burhanrashid52.photoeditor.*
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import kotlinx.android.synthetic.main.activity_edit_image.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class EditImageActivity :
    BaseActivity(), OnPhotoEditorListener,
    View.OnClickListener, PropertiesBSFragment.Properties, EmojiListener,
    StickerListener, OnItemSelected,
    FilterListener {
    var mPhotoEditor: PhotoEditor? = null
    private var mPhotoEditorView: PhotoEditorView? = null
    private var mPropertiesBSFragment: PropertiesBSFragment? = null
    private var mEmojiBSFragment: EmojiBSFragment? = null
    private var mStickerBSFragment: StickerBSFragment? = null
    private var mTxtCurrentTool: TextView? = null
    private var mWonderFont: Typeface? = null
    private var mRvTools: RecyclerView? = null
    private var mRvFilters: RecyclerView? = null
    private val mEditingToolsAdapter = EditingToolsAdapter(this)
    private val mFilterViewAdapter = FilterViewAdapter(this)
    private var mRootView: ConstraintLayout? = null
    private val mConstraintSet = ConstraintSet()
    private var mIsFilterVisible = false
    val PREFERENCE = "com.shimhg02.honbab"

    @VisibleForTesting
    var mSaveImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.activity_edit_image)
        checkPermission()
        initViews()

        handleIntentImage(mPhotoEditorView!!.source)
        mWonderFont = Typeface.createFromAsset(assets, "beyond_wonderland.ttf")
        mPropertiesBSFragment = PropertiesBSFragment()
        mEmojiBSFragment = EmojiBSFragment()
        mStickerBSFragment = StickerBSFragment()
        mStickerBSFragment!!.setStickerListener(this)
        mEmojiBSFragment!!.setEmojiListener(this)
        mPropertiesBSFragment!!.setPropertiesChangeListener(this)
        val llmTools =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRvTools!!.layoutManager = llmTools
        mRvTools!!.adapter = mEditingToolsAdapter
        val llmFilters =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRvFilters!!.layoutManager = llmFilters
        mRvFilters!!.adapter = mFilterViewAdapter
        storyUpload.setOnClickListener {
            saveStory()
        }

        //Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);
        //Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");
        mPhotoEditor = PhotoEditor.Builder(this, mPhotoEditorView)
            .setPinchTextScalable(true) // set flag to make text scalable when pinch
            //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build() // build photo editor sdk
        mPhotoEditor!!.setOnPhotoEditorListener(this)

        //Set Image Dynamically
        mPhotoEditorView!!.source.setImageResource(R.drawable.chiken)
    }

    private fun handleIntentImage(source: ImageView) {
        val intent = intent
        if (intent != null) {
            val intentType = intent.type
            if (intentType != null && intentType.startsWith("image/")) {
                val imageUri = intent.data
                if (imageUri != null) {
                    source.setImageURI(imageUri)
                }
            }
        }
    }

    private fun initViews() {
        val imgUndo: ImageView
        val imgRedo: ImageView
        val imgCamera: ImageView
        val imgGallery: ImageView
        val imgSave: ImageView
        val imgClose: ImageView
        val imgShare: ImageView
        mPhotoEditorView = findViewById(R.id.photoEditorView)
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool)
        mRvTools = findViewById(R.id.rvConstraintTools)
        mRvFilters = findViewById(R.id.rvFilterView)
        mRootView = findViewById(R.id.rootView)
        imgUndo = findViewById(R.id.imgUndo)
        imgUndo.setOnClickListener(this)
        imgRedo = findViewById(R.id.imgRedo)
        imgRedo.setOnClickListener(this)
        imgCamera = findViewById(R.id.imgCamera)
        imgCamera.setOnClickListener(this)
        imgGallery = findViewById(R.id.imgGallery)
        imgGallery.setOnClickListener(this)
        imgSave = findViewById(R.id.imgSave)
        imgSave.setOnClickListener(this)
        imgClose = findViewById(R.id.imgClose)
        imgClose.setOnClickListener(this)
        imgShare = findViewById(R.id.imgShare)
        imgShare.setOnClickListener(this)
    }

    override fun onEditTextChangeListener(
        rootView: View,
        text: String,
        colorCode: Int
    ) {
        val textEditorDialogFragment =
            TextEditorDialogFragment.show(this, text, colorCode)
        textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode ->
            val styleBuilder = TextStyleBuilder()
            styleBuilder.withTextColor(colorCode)
            mPhotoEditor!!.editText(rootView, inputText, styleBuilder)
            mTxtCurrentTool!!.setText(R.string.label_text)
        }
    }

    override fun onAddViewListener(
        viewType: ViewType,
        numberOfAddedViews: Int
    ) {
        Log.d(
            TAG,
            "onAddViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    override fun onRemoveViewListener(
        viewType: ViewType,
        numberOfAddedViews: Int
    ) {
        Log.d(
            TAG,
            "onRemoveViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    override fun onStartViewChangeListener(viewType: ViewType) {
        Log.d(
            TAG,
            "onStartViewChangeListener() called with: viewType = [$viewType]"
        )
    }

    override fun onStopViewChangeListener(viewType: ViewType) {
        Log.d(
            TAG,
            "onStopViewChangeListener() called with: viewType = [$viewType]"
        )
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgUndo -> mPhotoEditor!!.undo()
            R.id.imgRedo -> mPhotoEditor!!.redo()
            R.id.imgSave -> saveImage()
            R.id.imgClose -> onBackPressed()
            R.id.imgShare -> try {
                shareImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            R.id.imgCamera -> {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
            R.id.imgGallery -> {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    PICK_REQUEST
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Throws(IOException::class)
    private fun shareImage() {
        checkPermission()
        showLoading("저장중...")
        val file = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + ""
                    + System.currentTimeMillis() + ".png"
        )
        try {
            file.createNewFile()
            val saveSettings = SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(true)
                .build()
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mPhotoEditor!!.saveAsFile(
                file.absolutePath,
                saveSettings,
                object : OnSaveListener {
                    override fun onSuccess(imagePath: String) {
                        hideLoading()
                        showSnackbar("이미지 저장 성공!")
                        mSaveImageUri = Uri.fromFile(File(imagePath))
                        mPhotoEditorView!!.source.setImageURI(mSaveImageUri)
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_STREAM, buildFileProviderUri(mSaveImageUri!!))
                        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)))
                    }

                    override fun onFailure(exception: Exception) {
                        hideLoading()
                        showSnackbar("이미지 저장 실패, 다시 시도해주세요!")
                    }
                })
        } catch (e: IOException) {
            e.printStackTrace()
            hideLoading()
            showSnackbar(e.message!!)
        }
    }

    private fun buildFileProviderUri(uri: Uri): Uri {
        return FileProvider.getUriForFile(
            this,
            FILE_PROVIDER_AUTHORITY,
            File(uri.path)
        )
    }

    private fun saveStory(){
        showLoading("저장중...")
        val file = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + ""
                    + System.currentTimeMillis() + ".png"
        )
        try {
            file.createNewFile()
            val saveSettings = SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(true)
                .build()
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mPhotoEditor!!.saveAsFile(
                file.absolutePath,
                saveSettings,
                object : OnSaveListener {
                    override fun onSuccess(imagePath: String) {
                        hideLoading()
                        showSnackbar("이미지 저장 완료")
                        mSaveImageUri = Uri.fromFile(File(imagePath))
                        mPhotoEditorView!!.source.setImageURI(mSaveImageUri)
                        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            this@EditImageActivity.getContentResolver(),
                            mSaveImageUri
                        )
                        showLoading("업로드중... \n치킨을 드셨나요? 맛있으셨을것 같아요! ")
                        var base64ImgString =  bitmap.encodeBitmapIntoBase64(Bitmap.CompressFormat.PNG)
                        System.out.println("TEST IMG: $base64ImgString")
                        Client.retrofitService.addStory(pref.getString("token","").toString(), base64ImgString).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                                when (response!!.code()) {
                                    200 -> {
                                        hideLoading()
                                        startActivity<MainActivity>()
                                        finish()
                                    }
                                    203-> {
                                    }
                                    500 -> Toast.makeText(this@EditImageActivity, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                                }
                            }
                            override fun onFailure(call: Call<Void>?, t: Throwable?) {

                            }
                        })
                    }

                    override fun onFailure(exception: Exception) {
                        hideLoading()
                        showSnackbar("이미지 업로드 실패, 다시 시도해주세요")
                    }
                })
        } catch (e: IOException) {
            e.printStackTrace()
            hideLoading()
            showSnackbar(e.message!!)
        }
    }

    private fun saveImage() {
        showLoading("저장중...")
        val file = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + ""
                    + System.currentTimeMillis() + ".png"
        )
        try {
            file.createNewFile()
            val saveSettings = SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(true)
                .build()
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mPhotoEditor!!.saveAsFile(
                file.absolutePath,
                saveSettings,
                object : OnSaveListener {
                    override fun onSuccess(imagePath: String) {
                        hideLoading()
                        showSnackbar("이미지 저장 완료!")
                        mSaveImageUri = Uri.fromFile(File(imagePath))
                        mPhotoEditorView!!.source.setImageURI(mSaveImageUri)
                    }

                    override fun onFailure(exception: Exception) {
                        hideLoading()
                        showSnackbar("이미지 저장 실패, 다시 시도해 주세요.")
                    }
                })
        } catch (e: IOException) {
            e.printStackTrace()
            hideLoading()
            showSnackbar(e.message!!)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    mPhotoEditor!!.clearAllViews()
                    val photo = data!!.extras!!["data"] as Bitmap?
                    mPhotoEditorView!!.source.setImageBitmap(photo)
                }
                PICK_REQUEST -> try {
                    mPhotoEditor!!.clearAllViews()
                    val uri = data!!.data
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    mPhotoEditorView!!.source.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onColorChanged(colorCode: Int) {
        mPhotoEditor!!.brushColor = colorCode
        mTxtCurrentTool!!.setText(R.string.label_brush)
    }

    override fun onOpacityChanged(opacity: Int) {
        mPhotoEditor!!.setOpacity(opacity)
        mTxtCurrentTool!!.setText(R.string.label_brush)
    }

    override fun onBrushSizeChanged(brushSize: Int) {
        mPhotoEditor!!.brushSize = brushSize.toFloat()
        mTxtCurrentTool!!.setText(R.string.label_brush)
    }

    override fun onEmojiClick(emojiUnicode: String?) {
        mPhotoEditor!!.addEmoji(emojiUnicode)
        mTxtCurrentTool!!.setText(R.string.label_emoji)
    }

    override fun isPermissionGranted(isGranted: Boolean, permission: String?) {
        if (isGranted) {
            saveImage()
        }
    }

    private fun showSaveDialog() {
        val builder =
            AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.msg_save_image))
        builder.setPositiveButton("Save") { dialog, which -> saveImage() }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        builder.setNeutralButton("Discard") { dialog, which -> finish() }
        builder.create().show()
    }

    override fun onFilterSelected(photoFilter: PhotoFilter?) {
        mPhotoEditor!!.setFilterEffect(photoFilter)
    }

    override fun onToolSelected(toolType: ToolType?) {
        when (toolType) {
            ToolType.BRUSH -> {
                mPhotoEditor!!.setBrushDrawingMode(true)
                mTxtCurrentTool!!.setText(R.string.label_brush)
                mPropertiesBSFragment!!.show(
                    supportFragmentManager,
                    mPropertiesBSFragment!!.tag
                )
            }
            ToolType.TEXT -> {
                val textEditorDialogFragment =
                    TextEditorDialogFragment.show(this)
                textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode ->
                    val styleBuilder = TextStyleBuilder()
                    styleBuilder.withTextColor(colorCode)
                    mPhotoEditor!!.addText(inputText, styleBuilder)
                    mTxtCurrentTool!!.setText(R.string.label_text)
                }
            }
            ToolType.ERASER -> {
                mPhotoEditor!!.brushEraser()
                mTxtCurrentTool!!.setText(R.string.label_eraser_mode)
            }
            ToolType.FILTER -> {
                mTxtCurrentTool!!.setText(R.string.label_filter)
                showFilter(true)
            }
            ToolType.EMOJI -> mEmojiBSFragment!!.show(
                supportFragmentManager,
                mEmojiBSFragment!!.tag
            )
            ToolType.STICKER -> mStickerBSFragment!!.show(
                supportFragmentManager,
                mStickerBSFragment!!.tag
            )
        }
    }

    fun showFilter(isVisible: Boolean) {
        mIsFilterVisible = isVisible
        mConstraintSet.clone(mRootView)
        if (isVisible) {
            mConstraintSet.clear(mRvFilters!!.id, ConstraintSet.START)
            mConstraintSet.connect(
                mRvFilters!!.id, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START
            )
            mConstraintSet.connect(
                mRvFilters!!.id, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
        } else {
            mConstraintSet.connect(
                mRvFilters!!.id, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
            mConstraintSet.clear(mRvFilters!!.id, ConstraintSet.END)
        }
        val changeBounds = ChangeBounds()
        changeBounds.duration = 350
        changeBounds.interpolator = AnticipateOvershootInterpolator(1.0f)
        TransitionManager.beginDelayedTransition(mRootView!!, changeBounds)
        mConstraintSet.applyTo(mRootView)
    }

    override fun onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false)
            mTxtCurrentTool!!.setText(R.string.app_name)
        } else if (!mPhotoEditor!!.isCacheEmpty) {
            showSaveDialog()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private val TAG = EditImageActivity::class.java.simpleName
        const val FILE_PROVIDER_AUTHORITY = "com.shimhg02.solorestorant" + ".provider"
        private const val CAMERA_REQUEST = 52
        private const val PICK_REQUEST = 53
    }

    override fun onStickerClick(bitmap: Bitmap?) {
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        { // 마시멜로우 버전과 같거나 이상이라면
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED))
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show()
                }
                requestPermissions(arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    2) //마지막 인자는 체크해야될 권한 갯수
            }
            else
            {
                //Toast.makeText(this, "권한 승인되었음", Toast.LENGTH_SHORT).show();
            }
        }
    }
}