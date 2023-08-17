package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShowRoomActivity : AppCompatActivity() {
    private lateinit var showroomAdapter : ShowRoomAdapter
    private var setM2 : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_room)

        val heartImageView: ImageView = findViewById(R.id.show_heart)
        heartImageView.setOnClickListener{
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            finish()
        }

        val checkImageView: ImageView = findViewById(R.id.show_check)
        checkImageView.setOnClickListener{
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            finish()
        }

        val chatImageView: ImageView = findViewById(R.id.show_chat)
        chatImageView.setOnClickListener{
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profileImageView: ImageView = findViewById(R.id.show_profile)
        profileImageView.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }



        // 더미 데이터 리스트 예시
        val roomList = arrayListOf(
            room_data(R.drawable.home, "아파트", "큰 방", 5,16.7,
                7,100, 30,"광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,16.7,
                7,100, 30,"광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,16.7,
                7,100, 30,"광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,16.7,
                7,100, 30,"광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,16.7,
                7,100, 30,"광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,16.7,
                7,100, 30,"광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,16.7,
                7,100, 30,"광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,16.7,
                7,100, 30,"광진구","건국대 도보 5분"),
        )

        //roomlist 개수
        val sumOfRoom = findViewById<TextView>(R.id.CountRoom)
        sumOfRoom.text = "총 ${roomList.size} 개"

        //리사이클러뷰
        val rv_room = findViewById<RecyclerView>(R.id.room_list)
        rv_room.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_room.setHasFixedSize(true)
        showroomAdapter = ShowRoomAdapter(roomList, setM2) // 어댑터 생성 및 초기화
        rv_room.adapter = showroomAdapter


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