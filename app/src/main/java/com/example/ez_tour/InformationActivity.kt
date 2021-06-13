package com.example.ez_tour

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.user.UserApiClient
import java.net.URL

class InformationActivity : AppCompatActivity() {


    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference()
    var tag: String = ""
    var name: String = ""
    var image: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        tag = intent.getStringExtra("tag").replace("[","").replace("]","")
        name = intent.getStringExtra("name").replace("[","").replace("]","")
       val view_inimage = findViewById<ImageView>(R.id.view_Inimage)
        val text_inname = findViewById<TextView>(R.id.text_inname)
        val text_inaddress = findViewById<TextView>(R.id.text_inaddress)
        val btn_star = findViewById<ImageButton>(R.id.btn_star)
        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        val btn_search = findViewById<Button>(R.id.btn_search)
        val btn_map = findViewById<Button>(R.id.btn_map)
        val btn_mypage = findViewById<Button>(R.id.btn_mypage)
        val btn_navi = findViewById<ImageButton>(R.id.btn_navi)
        val btn_internet = findViewById<ImageButton>(R.id.btn_internet)
        val btn_share = findViewById<ImageButton>(R.id.btn_share)
        var star: Int = 0
        Log.d("Datatest","${tag}")
        Log.d("Datatest","${name}")


        text_inname.text = name
        databaseReference.child(tag).child(name).get().addOnSuccessListener {
            text_inaddress.text = "${it.child("주소").value}"
            var image_task: URLtoBitmapTask = URLtoBitmapTask()
            image_task = URLtoBitmapTask().apply {
                try {
                    if(it.child("이미지URL").getValue() != "NULL"){
                        url = URL("${it.child("이미지URL").getValue()}")
                        image = "${it.child("이미지URL").getValue()}"
                    } else {
                        url = URL("https://artsmidnorthcoast.com/wp-content/uploads/2014/05/no-image-available-icon-6.png")
                        image = "https://artsmidnorthcoast.com/wp-content/uploads/2014/05/no-image-available-icon-6.png"
                    }
                }catch (e: Exception){
                    url = URL("https://artsmidnorthcoast.com/wp-content/uploads/2014/05/no-image-available-icon-6.png")
                }

            }
            var bitmap: Bitmap = image_task.execute().get()
            view_inimage.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Log.d("Failtest","실패")
        }

        Log.d("Datatest","${databaseReference.child(tag).child(name).child("주소").get()}")
       UserApiClient.instance.me { user, error ->
            databaseReference.child("사용자").child("${user!!.id}").child("즐겨찾기")
                    .child(name).child("이름").get().addOnSuccessListener {
                   if (it.value == name) {
                       btn_star.setImageResource(R.drawable.map_image_0003_heart_1)
                       star = 1

                   } else{
                       btn_star.setImageResource(R.drawable.map_image_0004_heart_2)
                       star = 0
                   }
                }
        }

        btn_star.setOnClickListener {
            UserApiClient.instance.me { user, error ->
                if (star == 1) {
                    btn_star.setImageResource(R.drawable.map_image_0004_heart_2)
                    star = 0
                    databaseReference.child("사용자").child("${user!!.id}").child("즐겨찾기")
                        .child(name).removeValue()

                    Toast.makeText(this, "즐겨찾기에서 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    btn_star.setImageResource(R.drawable.map_image_0003_heart_1)
                    star = 1
                    databaseReference.child("사용자").child("${user!!.id}").child("즐겨찾기")
                        .child(name).child("이름").setValue(name)
                    databaseReference.child("사용자").child("${user!!.id}").child("즐겨찾기")
                        .child(name).child("태그").setValue(tag)
                    databaseReference.child("사용자").child("${user!!.id}").child("즐겨찾기")
                        .child(name).child("이미지URL").setValue(image)

                    Toast.makeText(this, "즐겨찾기에 추가하였습니다.", Toast.LENGTH_SHORT).show()
                }

            }


        }

        btn_internet.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val itemList = arrayOf("확인", "취소")
            builder.setTitle("인터넷에서 검색하시겠습니까?")
            builder.setItems(itemList) { dialog, which ->
                when(which) {
                    0 -> {
                        val address = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+"${name}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
                        startActivity(intent)
                    }  // 토스트
                    1 -> dialog.dismiss()
                }
            }
            builder.show()
        }

        btn_navi.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val itemList = arrayOf("확인", "취소")
            builder.setTitle("카카오맵에서 검색하시겠습니까?")
            builder.setItems(itemList) { dialog, which ->
                when(which) {
                    0 -> {
                        try {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse("kakaomap://search?q=${name}")).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            }
                        } catch (e: NullPointerException) {
                            Log.e("LOCATION_ERROR", e.toString())
                            val builder = AlertDialog.Builder(this)
                            val itemList = arrayOf("확인", "취소")
                            builder.setTitle("카카오맵 설치 페이지로 넘어가시겠습니까?")
                            builder.setItems(itemList) { dialog, which ->
                                when(which) {
                                    0 -> {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("\"https://play.google.com/store/apps/details?id=카카오맵")
                                        ).apply {
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        if (intent.resolveActivity(packageManager) != null) {
                                            startActivity(intent)
                                        }
                                    }  // 토스트
                                    1 -> dialog.dismiss()
                                }
                            }
                            builder.show()
                        }
                    }  // 토스트
                    1 -> dialog.dismiss()
                }
            }
            builder.show()
        }

        btn_share.setOnClickListener {
            sendKakaoLink(this)
        }

        btn_back.setOnClickListener {
            finish()
        }

        btn_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // mypage 버튼 클릭리스너
        btn_map.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        btn_mypage.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }
    }
    private fun sendKakaoLink(context:Context) {
        val defaultFeed = FeedTemplate(
            content = Content(
                title = "${name}",
                description = "#충전소, #${tag}",
                imageUrl = image,
                link = Link(
                    webUrl = "https://developers.kakao.com",
                    mobileWebUrl = "https://developers.kakao.com"
                )
            ),
            )
        LinkClient.instance.defaultTemplate(context, defaultFeed) { linkResult, error ->
            if (error != null) {
                Log.e("LinkTest", "카카오링크 보내기 실패", error)
            }
            else if (linkResult != null) {
                Log.d("LinkTest", "카카오링크 보내기 성공 ${linkResult.intent}")
                startActivity(linkResult.intent)

                // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                Log.w("LinkTest", "Warning Msg: ${linkResult.warningMsg}")
                Log.w("LinkTest", "Argument Msg: ${linkResult.argumentMsg}")
            }
        }
    }
}