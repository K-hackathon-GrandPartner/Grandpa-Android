package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowRoomActivity : AppCompatActivity() {
    private lateinit var showroomAdapter : ShowRoomAdapter
    private var setM2 : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_room)

        //heart 화면
        val heartImageView: ImageView = findViewById(R.id.filtering_heart)
        heartImageView.setOnClickListener{
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            finish()
        }
        //check 화면
        val checkImageView: ImageView = findViewById(R.id.filtering_check)
        checkImageView.setOnClickListener{
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            finish()
        }
        //chat 화면
        val chatImageView: ImageView = findViewById(R.id.filtering_chat)
        chatImageView.setOnClickListener{
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }
        //profile 화면
        val profileImageView: ImageView = findViewById(R.id.filtering_profile)
        profileImageView.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        //retrofit2, 서버로 부터 data 받아오기
        //dataList 생성
        val roomList = arrayListOf<room_data>()
        // 서비스 객체 생성
        val service = ShowRoomImpl.service_ct_tab

        // API 요청
        val call = service.requestList(ShowRoomImpl.BASE_URL)

        call.enqueue(object : Callback<ShowRoomResponse> {
            override fun onResponse(
                call: Call<ShowRoomResponse>,
                response: Response<ShowRoomResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val responseData = apiResponse.result
                        for (data in responseData) {
                            //받아온 데이터 list에 넣음
                            val room = room_data(data.id, data.imageUrl, data.buildingType, data.roomSizeType, data.roomSize, data.roomFloor, data.deposit, data.monthlyRent, data.address, data.title, data.postDate)
                            roomList.add(room)
                        }

                        //roomlist 개수
                        val sumOfRoom = findViewById<TextView>(R.id.CountRoom)
                        sumOfRoom.text = "총 ${roomList.size} 개"

                        //리사이클러뷰
                        val rv_room = findViewById<RecyclerView>(R.id.room_list)
                        rv_room.layoutManager = LinearLayoutManager(this@ShowRoomActivity,LinearLayoutManager.VERTICAL,false)
                        rv_room.setHasFixedSize(true)
                        showroomAdapter = ShowRoomAdapter(roomList, setM2, this@ShowRoomActivity) // 어댑터 생성 및 초기화
                        rv_room.adapter = showroomAdapter
                    }
                } else {
                    // 서버 응답 실패 처리
                    Log.e("Response", "Response is not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ShowRoomResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("Response", "Network error: ${t.message}")
            }
        })


        //m2 평 전환
        val m2ImageView: ImageView = findViewById(R.id.m2)

        m2ImageView.setOnClickListener{
            if(setM2){
                setM2 = !setM2
                m2ImageView.setImageResource(R.drawable.m2korea)
                showroomAdapter.setM2Setting(setM2)
            }else{
                setM2 = !setM2
                m2ImageView.setImageResource(R.drawable.m2)
                showroomAdapter.setM2Setting(setM2)
            }
        }

        //filter Setting 화면으로
        val filterImageView: ImageView = findViewById(R.id.filter)
        filterImageView.setOnClickListener{
            val intent = Intent(this, FilterActivity::class.java)
            intent.putExtra("roomListSize", roomList.size) //put으로 roomList 사이즈 보냄
            startActivity(intent)
            finish()
        }

    }
}