package com.example.ez_tour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import java.util.*

class MainActivity : AppCompatActivity() {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //파싱코드 배포시 제거
       /* var file = InputStreamReader(getResources().openRawResource(R.raw.test))
        var fileReader: BufferedReader? = null
        var csvReader: CSVReader? = null
        val dataList = arrayListOf<Array<String>>()
        var n:Int = 0
        fileReader = BufferedReader(file)
        csvReader = CSVReader(fileReader)
        //파싱용 코드 배포용에서 제거 할것
        try {


            csvReader.use {
                for (data in it) {

                    dataList.add(data)

                }
            }
        } catch (e: IOException) {
            if(BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
        for(data in dataList) {
            Log.d("jhdroid_test", "data : ${Arrays.deepToString(data)}")
            databaseReference.child("${Arrays.deepToString(arrayOf(data[0])).replace("[","").replace("]","")}")
                .child("${Arrays.deepToString(arrayOf(data[1])).replace("[","").replace("]","")}")
                .child("이름").setValue("${Arrays.deepToString(arrayOf(data[1])).replace("[","").replace("]","")}","이름")
            databaseReference.child("${Arrays.deepToString(arrayOf(data[0])).replace("[","").replace("]","")}")
                .child("${Arrays.deepToString(arrayOf(data[1])).replace("[","").replace("]","")}")
                .child("급속충전타입구분").setValue("${Arrays.deepToString(arrayOf(data[2])).replace("[","").replace("]","")}","급속충전타입구분")
            databaseReference.child("${Arrays.deepToString(arrayOf(data[0])).replace("[","").replace("]","")}")
                .child("${Arrays.deepToString(arrayOf(data[1])).replace("[","").replace("]","")}")
                .child("주소").setValue("${Arrays.deepToString(arrayOf(data[3])).replace("[","").replace("]","")}","주소")
            databaseReference.child("${Arrays.deepToString(arrayOf(data[0])).replace("[","").replace("]","")}")
                .child("${Arrays.deepToString(arrayOf(data[1])).replace("[","").replace("]","")}").child("좌표")
                .child("위도").setValue("${Arrays.deepToString(arrayOf(data[4])).replace("[","").replace("]","")}","위도")
            databaseReference.child("${Arrays.deepToString(arrayOf(data[0])).replace("[","").replace("]","")}")
                .child("${Arrays.deepToString(arrayOf(data[1])).replace("[","").replace("]","")}").child("좌표")
                .child("경도").setValue("${Arrays.deepToString(arrayOf(data[5])).replace("[","").replace("]","")}","경도")
            databaseReference.child("${Arrays.deepToString(arrayOf(data[0])).replace("[","").replace("]","")}")
                .child("${Arrays.deepToString(arrayOf(data[1])).replace("[","").replace("]","")}")
                .child("태그").setValue("${Arrays.deepToString(arrayOf(data[0])).replace("[","").replace("]","")}","태그")
            databaseReference.child("${Arrays.deepToString(arrayOf(data[0])).replace("[","").replace("]","")}")
                .child("${Arrays.deepToString(arrayOf(data[1])).replace("[","").replace("]","")}")
                .child("이미지URL").setValue("${Arrays.deepToString(arrayOf(data[6])).replace("[","").replace("]","")}","이미지URL")
        } */
        //해쉬키 얻기
       /* try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = info.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")
            for (signature in signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val key = String(Base64.encode(md.digest(), 0))
                Log.d("Hash key:", "!!!!!!!$key!!!!!!")
            }
        } catch (e: Exception) {
            Log.e("name not found", e.toString())
        } */

        val btn_login = findViewById<Button>(R.id.btn_login) as ImageButton

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                        Log.d("LoginError",error.toString())
                    }
                }
            }
            else if (token != null) {   //로그인 성공
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                // 정보 저장하기
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Toast.makeText(this, "사용자 정보 요청 실패", Toast.LENGTH_SHORT).show()
                    } else if (user != null) {
                        databaseReference.child("사용자").child("${user.id}").child("카카오").child("ID").setValue("${user.id}","ID:")
                        databaseReference.child("사용자").child("${user.id}").child("카카오").child("닉네임").setValue("${user.kakaoAccount?.profile?.nickname}","닉네임")
                        databaseReference.child("사용자").child("${user.id}").child("카카오").child("프로필URL").setValue("${user.kakaoAccount?.profile?.thumbnailImageUrl}","프로필URL")
                    }
                }
            }
        }

        btn_login.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@MainActivity)) {
                    UserApiClient.instance.loginWithKakaoTalk(this@MainActivity, callback = callback)
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(this@MainActivity, callback = callback)
                }

        }
    }
}


