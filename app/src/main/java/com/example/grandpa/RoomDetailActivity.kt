package com.example.grandpa

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_detail) // filter_roomInfo.xml 파일과 연결

        val roomId = intent.getIntExtra("room_id", 0) //0은 기본 값
        Log.d("roomId", roomId.toString())


        // 서비스 객체 생성
        val service = DetailRoomImpl.service_ct_tab

        // API 요청
        val callUrl = DetailRoomImpl.BASE_URL + roomId.toString() + "/"
        val call = service.requestList(callUrl)

        call.enqueue(object: Callback<DetailRoomResponse>{
            override fun onResponse(
                call: Call<DetailRoomResponse>,
                response: Response<DetailRoomResponse>
            ){
                if(response.isSuccessful){
                    val apiResponse = response.body()
                    if(apiResponse != null){
                        val responseData = apiResponse.result
                        Log.d("responseData", responseData.toString())
                    }
                }else {
                    // 서버 응답 실패 처리
                    Log.e("Response", "Response is not successful. Code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<DetailRoomResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("Response", "Network error: ${t.message}")
            }
        })
    }
}