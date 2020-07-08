package com.shimhg02.solorestorant.ui.Fragment.Feed


import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shimhg02.solorestorant.Adapter.Story.StoryAdapter
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Activity.CateFoodActivity
import com.shimhg02.solorestorant.Test.Adapter.TestRecommendAdapter
import com.shimhg02.solorestorant.ui.Activity.ImageEditor.EditImageActivity
import com.shimhg02.solorestorant.network.Data.RecommendData.RecommendData
import com.shimhg02.solorestorant.network.Data.StoryData.StoryData
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Story.StoryActivity
import com.shimhg02.solorestorant.utils.Base64.encodeBitmapIntoBase64
import kotlinx.android.synthetic.main.fragment_feed.view.*
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @notice Test 그룹
 * @description 피드 프레그먼트
 */

@Suppress("DEPRECATION")
class FeedFragment : Fragment() { //프레그먼트를 띄우기 위해 주로 사용합니다.
    private var recyclerView: RecyclerView? = null
    private var recyclerView2: RecyclerView? = null
    private var recyclerView3: RecyclerView? = null
    private var adapterd2: TestRecommendAdapter? = null
    private val GET_GALLERY_IMAGE = 200
    private var adapter: StoryAdapter? = null
    val PREFERENCE = "com.shimhg02.honbab"
    private val items = java.util.ArrayList<StoryData>()

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val PREFERENCE = "com.shimhg02.honbab"
        val pref = requireActivity().getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val recommendTestDataList = arrayListOf( //테스트용 더미데이터2
            RecommendData(
                "람다람",
                "킹갓람다람",
                "https://s3.marpple.co/files/u_14345/2020/4/original/2144663c7991a33e33469e2d23a25591e1cd47e568e767c.png"
            ),
            RecommendData(
                "람다람",
                "람다람일러좋아",
                "https://cdn.class101.net/images/8062f865-c1ec-459c-b3f6-3d67a928cda7/1200x630"
            )
        )

        val recommendTestDataList2 = arrayListOf( //테스트용 더미데이터3
            RecommendData(
                "람다람",
                "람다람 넣을게~",
                "https://steamuserimages-a.akamaihd.net/ugc/781851765958068828/D4B37575DDB625FFAF9E6D81D5DB43F622D23E51/?imw=512&&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false"
            ),
            RecommendData(
                "람다람",
                "람다람 왜 울고있는거야?",
                "https://cdn.class101.net/images/0c204339-b159-45c9-bf28-1335bb48125f/1200xauto"
            )
        )

        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        recyclerView = view.findViewById(R.id.recycler_story)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        recyclerView?.adapter =  StoryAdapter(items)
        adapter = recyclerView!!.adapter as StoryAdapter?
        view.addStory.setOnClickListener {
            addStory()
        }
        val intentToCategory = Intent(activity, CateFoodActivity::class.java)
        view.btn1.setOnClickListener {
            intentToCategory.putExtra("foodName","치킨")
            startActivity(intentToCategory)
        }
        view.btn2.setOnClickListener {
            intentToCategory.putExtra("foodName","분식")
            startActivity(intentToCategory)
        }
        view.btn3.setOnClickListener {
            intentToCategory.putExtra("foodName","중식")
            startActivity(intentToCategory)
        }
        view.btn4.setOnClickListener {
            intentToCategory.putExtra("foodName","일식")
            startActivity(intentToCategory)
        }
        Client.retrofitService.getStory().enqueue(object :
            retrofit2.Callback<ArrayList<StoryData>> {
            override fun onResponse(call: Call<ArrayList<StoryData>>?, response: Response<ArrayList<StoryData>>?) {
                val repo = response!!.body()
                when (response.code()) {
                    200 -> {
                        repo!!.indices.forEach {
                            items += StoryData(
                                repo[it].alreadyWatch,
                                repo[it].createdAt,
                                repo[it].imgUrl,
                                repo[it].storyUUID,
                                repo[it].userName,
                                repo[it].imgUrl,
                                repo[it].userUUID
                            )
                            recyclerView!!.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<StoryData>>?, t: Throwable?) {
                Log.v("C2cTest", "fail!!")
            }
        })

        recyclerView2 = view.findViewById(R.id.recommend_foodView)
        recyclerView2?.setHasFixedSize(true)
        recyclerView2?.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView2?.adapter =  TestRecommendAdapter(recommendTestDataList)
        adapterd2 = recyclerView2?.adapter as TestRecommendAdapter?

        recyclerView3 = view.findViewById(R.id.recommend_foodView2)
        recyclerView3?.setHasFixedSize(true)
        recyclerView3?.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView3?.adapter =  TestRecommendAdapter(recommendTestDataList2)
        adapterd2 = recyclerView3?.adapter as TestRecommendAdapter?

        return view
    }

    fun addStory(){
        val pref = view?.context?.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        Client.retrofitService.checkStory(pref?.getString("token","").toString()).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                when (response!!.code()) {
                    200 -> {
                        Toast.makeText(view!!.context, "200OK", Toast.LENGTH_LONG).show()
                        alertStoryDialog()
                    }
                    404-> {
                        startActivity<EditImageActivity>()
                    }
                    500 -> Toast.makeText(view!!.context, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Void>?, t: Throwable?) {

            }
        })
    }

    private fun alertStoryDialog(){
        var dialog = AlertDialog.Builder(requireView().context)
        dialog.setTitle("이미 스토리가 있습니다.")
        dialog.setMessage("나홀로 밥집은 6시간에 한번씩, 인당 1 스토리 체제로 밥먹을때마다 올리는 간단한 스토리를 지향하고 있습니다. \n따라서 스토리를 새로 작성하시면 기존 스토리에 덮어씌워집니다. \n그래도 작성하시겠습니까?")
        dialog.setIcon(R.mipmap.ic_launcher)

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->{
                        startActivity<EditImageActivity>()
                    }
                    DialogInterface.BUTTON_NEGATIVE ->{

                    }
                }
            }
        }

        dialog.setPositiveButton("네",dialog_listener)
        dialog.setNegativeButton("아니오",dialog_listener)
        dialog.show()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            var bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
            var base64IMGString = bitmap.encodeBitmapIntoBase64(Bitmap.CompressFormat.PNG)
            Toast.makeText(view?.context, base64IMGString, Toast.LENGTH_SHORT).show()
            println("LOGD IMGSTR : $base64IMGString")
        }
    }
}