package com.example.ez_tour

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_search.*
import java.net.URL
import java.util.*

class MypageActivity : AppCompatActivity() {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val slist = ArrayList<StarData>()
        var count:Int = 0
        val nameData = ArrayList<String>()
        val tagData = ArrayList<String>()
        val StarView = findViewById<RecyclerView>(R.id.StarVIew)
        val adapter = StarAdapter(slist,this@MypageActivity)

        val btn_map = findViewById<Button>(R.id.btn_map)
        val btn_search = findViewById<Button>(R.id.btn_search)
        val btn_appinfo = findViewById<Button>(R.id.btn_appinfo) as ImageButton

        var text_nickname = findViewById<TextView>(R.id.text_nickname)
        var view_profile = findViewById<ImageView>(R.id.view_profile)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Toast.makeText(this, "사용자 정보 요청 실패", Toast.LENGTH_SHORT).show()
            } else if (user != null) {
                text_nickname.setText("${user.kakaoAccount?.profile?.nickname}"+" "+"님")
                var image_task: URLtoBitmapTask = URLtoBitmapTask()
                image_task = URLtoBitmapTask().apply {
                    url = URL("${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                }
                var bitmap: Bitmap = image_task.execute().get()
                view_profile.setImageBitmap(bitmap)
            }
        }



        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Toast.makeText(this, "사용자 정보 요청 실패", Toast.LENGTH_SHORT).show()
            } else if (user != null) {
                databaseReference.child("사용자").child("${user.id}").child("즐겨찾기").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot != null) {
                            dataSnapshot.children.forEach { i ->
                                Log.d("MainActivity", "Single ValueEventListener : " + i.getValue());
                                var image_task: URLtoBitmapTask = URLtoBitmapTask()
                                image_task = URLtoBitmapTask().apply {
                                    try {
                                        if(i.child("이미지URL").getValue() != "NULL"){
                                            url = URL("${i.child("이미지URL").getValue()}")
                                        } else {
                                            url = URL("https://artsmidnorthcoast.com/wp-content/uploads/2014/05/no-image-available-icon-6.png")
                                        }
                                    }catch (e: Exception){
                                        url = URL("https://artsmidnorthcoast.com/wp-content/uploads/2014/05/no-image-available-icon-6.png")
                                    }

                                }
                                var bitmap: Bitmap = image_task.execute().get()
                                nameData.add("${i.child("이름").getValue()}")
                                tagData.add("${i.child("태그").getValue()}")
                                slist.add(StarData(count,"${i.child("이름").getValue()}", "${i.child("태그").getValue()}",bitmap))
                                count++
                                Log.d("MainActivity", "Count:" + count)
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Datasnapshot is null", Toast.LENGTH_SHORT).show()
                        }
                        Log.d("StarListtest", slist.toString())
                        StarView.adapter = adapter
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

                }

        }
        Log.d("StarListtest", slist.toString())
       StarView.adapter = adapter
       StarView.setHasFixedSize(true)


        btn_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        // mypage 버튼 클릭리스너
        btn_map.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        btn_appinfo.setOnClickListener {
            val intent = Intent(this, AppinfoActivity::class.java)
            startActivity(intent)
        }

    }


}

