package com.example.grandpa

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        SignupLocationSchoolActivity.LoginTokenDB.init(this)
//        val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance().edit()
//        LoginTokenData.clear()
//        LoginTokenData.apply()

        //getHashKey()

        //3초 후에 다음 액티비티로 전환
        Handler(Looper.getMainLooper()).postDelayed({
            //유저 정보 삭제
            checkToken()
        },2000) //2초

        FilterActivity.FilteringDB.init(this)
        val filterDB = FilterActivity.FilteringDB.getInstance().edit()
        filterDB.clear()
        filterDB.apply()

        }
    fun checkToken(){
        SignupLocationSchoolActivity.LoginTokenDB.init(this)
        val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance()
        val token = LoginTokenData.getString("accessToken", null)
        Log.d("checkToken", token.toString()) //토큰 값 print

        val service = ShowRoomImpl.service_ct_tab
        val call = service.requestList(token.toString()) //토큰 header에 넣음

        call.enqueue(object : Callback<ShowRoomResponse> {
            override fun onResponse(call: Call<ShowRoomResponse>, response: Response<ShowRoomResponse>) {
                val statusCode = response.code() // 응답의 상태 코드를 가져옴

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null){
                        val intent = Intent(this@MainActivity, ShowRoomActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0);
                        finish()
                    }
                } else {
                    // 서버가 오류 응답을 반환한 경우 처리 하는 코드 추가 -> 토큰 만료
                    Log.d("error", "서버가 오류 응답 반환, 상태 코드: $statusCode")
                    Log.d("response", response.toString())
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0);
                    finish()

                }
            }

            override fun onFailure(call: Call<ShowRoomResponse>, t: Throwable) {
                // 네트워크 오류 발생 시 처리하는 코드 추가
                Log.e("Response", "Network error: ${t.message}")
            }
        })
    }

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }


}



