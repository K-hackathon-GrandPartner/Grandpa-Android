package com.example.grandpa

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //3초 후에 다음 액티비티로 전환
        Handler().postDelayed({
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish() //이전 액티비티 종료
        },3000) //3초

        // 서비스 객체 생성
        val service = GroupRetrofitServiceImpl.service_ct_tab

        // API 요청
        val call = service.requestList(GroupRetrofitServiceImpl.BASE_URL)

        call.enqueue(object : Callback<List<Data>> {
            override fun onResponse(
                call: Call<List<Data>>,
                response: Response<List<Data>>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        for (data in responseData) {
                            // 데이터 출력
                            Log.d("Response", "ID: ${data.id}, Login Type: ${data.login_type}, User Name: ${data.user_name}, Status: ${data.status}")
                        }
                    }
                } else {
                    // 서버 응답 실패 처리
                    Log.e("Response", "Response is not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("Response", "Network error: ${t.message}")
            }
        })

    }
}