package com.example.ez_tour

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_search.*
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
private val databaseReference: DatabaseReference = firebaseDatabase.getReference()
val list = ArrayList<RecycleData>()
var count:Int = 0
val nameData = ArrayList<String>()
val tagData = ArrayList<String>()
val slist = ArrayList<RecycleData>()
var page: Int = 0
private var oldestPostId:String = ""

class SearchActivity : AppCompatActivity() ,TextWatcher {
    val radapter = RecyclerAdapter(list,this@SearchActivity)
    val spadapter = RecyclerAdapter(slist,this@SearchActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val editText = findViewById<EditText>(R.id.searcBar)
        val mRecyclerView = findViewById<RecyclerView>(R.id.review)
        var sResult = String()
        val text_title = findViewById<TextView>(R.id.textView)
        text_title.setText("검색창에 키워드를 입력해보세요!")
        mRecyclerView.adapter = radapter
        fun calldata(category: String) {
            databaseReference.child(category).addValueEventListener(object : ValueEventListener {
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
                            list.add(RecycleData(count,"${i.child("이름").getValue()}", "${i.child("태그").getValue()}",bitmap))
                            count++
                            radapter.notifyDataSetChanged()
                            Log.d("MainActivity", "Count:" + radapter.items)
                            Log.d("MainActivity", "Count:" + count)
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Datasnapshot is null", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("Listtest1", list.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
       databaseReference.child("숙소").limitToFirst(10).addValueEventListener(object : ValueEventListener {
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
                        list.add(RecycleData(count,"${i.child("이름").getValue()}", "${i.child("태그").getValue()}",bitmap))
                        count++
                        Log.d("MainActivity", "Count:" + count)
                        oldestPostId = i.getKey().toString()
                        Log.d("MainActivity", "Count:" + oldestPostId)
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Datasnapshot is null", Toast.LENGTH_SHORT).show()
                }
                Log.d("Listtest1", list.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        calldata("카페")
        mRecyclerView.adapter = radapter
        radapter.notifyDataSetChanged()
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1
                recyclerView.adapter!!.notifyDataSetChanged()
                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition == itemTotalCount) {
                    Log.d("ScrollTest","잘 되는가?")
                    databaseReference.child("숙소").orderByKey().startAt(oldestPostId).limitToFirst(10).addValueEventListener(object : ValueEventListener {
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
                                    list.add(RecycleData(count,"${i.child("이름").getValue()}", "${i.child("태그").getValue()}",bitmap))
                                    count++
                                    Log.d("MainActivity", "Count:" + count)
                                    oldestPostId = i.getKey().toString()
                                    Log.d("MainActivity", "Count:" + oldestPostId)
                                    radapter.notifyDataSetChanged()
                                }
                            } else {
                            }
                            Log.d("Listtest1", list.toString())
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                }
            }
        })

       // calldata("숙소")
       // calldata("음식점")
       // calldata("상점")
       // calldata("일반 충전소")

        mRecyclerView.setHasFixedSize(true)

        editText.addTextChangedListener(this)
        var sData = resources.getStringArray(R.array.spinnerdata)
        var sadapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sData)
        spinner.adapter = sadapter
        spinner.setSelection(0)
        fun spinnercall(ndata:String){
            databaseReference.child("${ndata}").addValueEventListener(object : ValueEventListener {
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
                            slist.add(RecycleData(count,"${i.child("이름").getValue()}", "${i.child("태그").getValue()}",bitmap))
                            count++
                            Log.d("MainActivity", "Count:" + count)
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Datasnapshot is null", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("Listtest1", "${ Arrays.deepToString(arrayOf(list))}")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            try {
                mRecyclerView.adapter = spadapter
                page = 1
            } catch (e: Exception) {
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sResult = sData.get(position).toString()
                if (sResult.equals("전체")){
                    try {
                        mRecyclerView.adapter = radapter
                        page = 0
                    } catch (e: Exception) {
                    }
                }else if (sResult.equals("일반 충전소")) {
                    spinnercall("일반충전소")
                }else if (sResult.equals("상점")){
                    spinnercall("상점")
                }else if (sResult.equals("음식점")){
                    spinnercall("음식점")
                }else if (sResult.equals("숙소")){
                    spinnercall("숙소")
                }else if (sResult.equals("카페")){
                    spinnercall("카페")
                }else {

                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val btn_map = findViewById<Button>(R.id.btn_map)
        val btn_mypage = findViewById<Button>(R.id.btn_mypage)

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

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(page == 0) {
        radapter.getFilter().filter(s.toString())
    }else{
        spadapter.getFilter().filter(s.toString())
        }
    }

    override fun afterTextChanged(s: Editable?) {}


}