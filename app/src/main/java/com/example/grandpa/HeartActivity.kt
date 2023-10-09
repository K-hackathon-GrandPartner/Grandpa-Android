package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class HeartActivity : AppCompatActivity(){
    private lateinit var heartAdapter : HeartAdapter
    private var setM2 : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.heart_room)

        //heart 화면
        val searchImageView: ImageView = findViewById(R.id.heart_search)

        searchImageView.setOnClickListener{
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }
        //check 화면
        val checkImageView: ImageView = findViewById(R.id.heart_check)
        checkImageView.setOnClickListener{
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }
        //chat 화면
        val chatImageView: ImageView = findViewById(R.id.heart_magazine)
        chatImageView.setOnClickListener{
            val intent = Intent(this, MagazineActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }
        //profile 화면
        val profileImageView: ImageView = findViewById(R.id.heart_profile)
        profileImageView.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        //m2 평 전환
        val m2ImageView: ImageView = findViewById(R.id.heart_m2)

        m2ImageView.setOnClickListener{
            if(setM2){
                setM2 = !setM2
                m2ImageView.setImageResource(R.drawable.m2)
                heartAdapter.setM2Setting(setM2)
            }else{
                setM2 = !setM2
                m2ImageView.setImageResource(R.drawable.m2korea)
                heartAdapter.setM2Setting(setM2)
            }
        }

        RoomDetailActivity.HeartDB.init(this)
        val InfoData = RoomDetailActivity.HeartDB.getInstance()
        val allEntries: Map<String, *> = InfoData.all
        //dataList 생성
        val roomList = arrayListOf<room_detail_data>()

        if(allEntries.isEmpty()){
            Log.d("하트 내부저장소", "없음")
        }else{
            //Log.d("하트 내부저장소", allEntries.toString())
            for ((key, value) in allEntries){
                if (value is String) {
                    val gson = Gson()
                    val roomInfo = gson.fromJson(value, room_detail_data::class.java)
                    roomList.add(roomInfo)
                }
            }
        }

        //roomlist 개수
        val sumOfRoom = findViewById<TextView>(R.id.heart_CountRoom)
        sumOfRoom.text = "총 ${roomList.size} 개"

        //리사이클러뷰
        val rv_room = findViewById<RecyclerView>(R.id.heartroom_list)
        rv_room.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        rv_room.setHasFixedSize(true)
        heartAdapter = HeartAdapter(roomList, setM2, this) // 어댑터 생성 및 초기화
        rv_room.adapter = heartAdapter
    }

    override fun onResume() {
        super.onResume()

        RoomDetailActivity.HeartDB.init(this)
        val InfoData = RoomDetailActivity.HeartDB.getInstance()
        val allEntries: Map<String, *> = InfoData.all
        //dataList 생성
        val roomList = arrayListOf<room_detail_data>()

        if(allEntries.isEmpty()){
            Log.d("하트 내부저장소", "없음")
        }else{
            //Log.d("하트 내부저장소", allEntries.toString())
            for ((key, value) in allEntries){
                if (value is String) {
                    val gson = Gson()
                    val roomInfo = gson.fromJson(value, room_detail_data::class.java)
                    roomList.add(roomInfo)
                }
            }
        }

        heartAdapter.updateData(roomList)
    }

}