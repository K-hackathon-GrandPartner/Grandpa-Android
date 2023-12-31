package com.example.grandpa

import android.annotation.SuppressLint
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
    private var setM2 : Boolean = false

    //dataList 생성
    val roomList = arrayListOf<room_data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_room)

        //heart 화면
        val heartImageView: ImageView = findViewById(R.id.showroom_heart)
        heartImageView.setOnClickListener{
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }
        //check 화면
        val checkImageView: ImageView = findViewById(R.id.showroom_check)
        checkImageView.setOnClickListener{
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }
        //chat 화면
        val chatImageView: ImageView = findViewById(R.id.showroom_magazine)
        chatImageView.setOnClickListener{
            val intent = Intent(this, MagazineActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }
        //profile 화면
        val profileImageView: ImageView = findViewById(R.id.showroom_profile)
        profileImageView.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        //m2 평 전환
        val m2ImageView: ImageView = findViewById(R.id.show_m2)

        m2ImageView.setOnClickListener{
            if(setM2){
                setM2 = !setM2
                m2ImageView.setImageResource(R.drawable.m2)
                showroomAdapter.setM2Setting(setM2)
            }else{
                setM2 = !setM2
                m2ImageView.setImageResource(R.drawable.m2korea)
                showroomAdapter.setM2Setting(setM2)
            }
        }

        //filter Setting 화면으로
        val filterImageView: ImageView = findViewById(R.id.show_filter)
        filterImageView.setOnClickListener{
            val intent = Intent(this, FilterActivity::class.java)
            intent.putExtra("roomListSize", roomList.size) //put으로 roomList 사이즈 보냄
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
            finish()
        }

        FilterActivity.FilteringDB.init(this)
        val filterDB = FilterActivity.FilteringDB.getInstance()
        val keys = filterDB.all.keys // 모든 SharedPreferences 키를 가져옴

        //내부저장소에 key값이 있다면
        if(keys.size != 0){
            getFilteredRooms() //필터링 된 데이터
        } else {
            getDefaultRooms() //필터링 안한 데이터
        }
    }

    //필터링 적용 안된 방 데이터 get 함수
    private fun getDefaultRooms() {
        // 서비스 객체 생성
        val service = ShowRoomImpl.service_ct_tab

        val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance()
        val token = LoginTokenData.getString("accessToken", null)

        val call = service.requestList(token.toString())

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
                        val sumOfRoom = findViewById<TextView>(R.id.show_CountRoom)
                        sumOfRoom.text = "${roomList.size}"

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
    }

    //필터링 값 URI 생성&get 함수
    @SuppressLint("CommitPrefEdits")
    fun getFilteredRooms() {
        //SharedPreferences 초기화
        FilterActivity.FilteringDB.init(this)
        val filterDB = FilterActivity.FilteringDB.getInstance()
        val keys = filterDB.all.keys // 모든 SharedPreferences 키를 가져옴

        // 쿼리 파라미터를 Map으로 만들기(보증금,월세)
        val queryParams = mutableMapOf<String,Float>()
        val regionParams = mutableListOf<String>()
        val buildingTypeParams = mutableListOf<String>()
        val roomSizeParams = mutableListOf<String>()
        val optionParams = mutableListOf<String>()

        for (key in keys) {
            if(key.contains("start") || key.contains("end")){
                val value = filterDB.all[key]
                queryParams[key] = value as Float
            }
            else if(key.contains("region")){
                val value = filterDB.all[key]
                regionParams.add(value.toString())
            }
            else if(key.contains("buildingType")){
                val value = filterDB.all[key]
                buildingTypeParams.add(value.toString())
            }
            else if(key.contains("roomSize")){
                val value = filterDB.all[key]
                roomSizeParams.add(value.toString())
            }
            else if(key.contains("option")){
                val value = filterDB.all[key]
                optionParams.add(value.toString())
            }
        }

        Log.d("getFilteredRooms","필터링 함수 실행됨")

        val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance()
        val token = LoginTokenData.getString("accessToken", null)

        // Retrofit 서비스 인터페이스를 사용하여 API 요청 보내기
        val call = FilteredRoomImpl.service_ct_tab.requestList(token.toString(),queryParams, regionParams, buildingTypeParams, roomSizeParams, optionParams)

        Log.d("call", call.toString())
        call.enqueue(object : Callback<ShowRoomResponse> {
            override fun onResponse(call: Call<ShowRoomResponse>, response: Response<ShowRoomResponse>) {
                Log.d("response", response.toString())
                if (response.isSuccessful) {
                    val filterResponse = response.body()
                    if (filterResponse != null) {
                        val responseData = filterResponse.result
                        Log.d("responseData", responseData.toString())
                        for (data in responseData) {
                            //받아온 데이터 list에 넣음
                            val room = room_data(data.id, data.imageUrl, data.buildingType, data.roomSizeType, data.roomSize, data.roomFloor, data.deposit, data.monthlyRent, data.address, data.title, data.postDate)
                            roomList.add(room)
                        }

                        //roomlist 개수
                        val sumOfRoom = findViewById<TextView>(R.id.show_CountRoom)
                        sumOfRoom.text = "${roomList.size}"

                        //리사이클러뷰
                        val rv_room = findViewById<RecyclerView>(R.id.room_list)
                        rv_room.layoutManager = LinearLayoutManager(this@ShowRoomActivity,LinearLayoutManager.VERTICAL,false)
                        rv_room.setHasFixedSize(true)
                        showroomAdapter = ShowRoomAdapter(roomList, setM2, this@ShowRoomActivity) // 어댑터 생성 및 초기화
                        rv_room.adapter = showroomAdapter
                    }
                } else {
                    // 응답 실패
                }
            }

            override fun onFailure(call: Call<ShowRoomResponse>, t: Throwable) {
                // 네트워크 오류 또는 예외 처리
                Log.d("API1", "fail")
            }
        })
    }
}