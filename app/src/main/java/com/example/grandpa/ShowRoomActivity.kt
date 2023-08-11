package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShowRoomActivity : AppCompatActivity() {
    private lateinit var showroomAdapter : ShowRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_room)

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

        val rv_room = findViewById<RecyclerView>(R.id.room_list)
        val sumOfRoom = findViewById<TextView>(R.id.CountRoom)

        sumOfRoom.text = "총 ${roomList.size} 개"

        rv_room.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_room.setHasFixedSize(true)
        showroomAdapter = ShowRoomAdapter(roomList, false) // 어댑터 생성 및 초기화
        rv_room.adapter = showroomAdapter

        val m2ImageView: ImageView = findViewById(R.id.m2)
        val m2koreaImageView: ImageView = findViewById(R.id.m2korea)

        //m2 to 평
        m2ImageView.setOnClickListener{
            m2ImageView.isVisible = m2ImageView.isInvisible
            m2koreaImageView.isInvisible = m2koreaImageView.isVisible
            showroomAdapter.setM2Setting(true)
        }
        //평 to m2
        m2koreaImageView.setOnClickListener{
            m2koreaImageView.isVisible = m2koreaImageView.isInvisible
            m2ImageView.isInvisible = m2ImageView.isVisible
            showroomAdapter.setM2Setting(false)
        }

        //filter Setting 화면으로
        val filterImageView: ImageView = findViewById(R.id.filter)
        filterImageView.setOnClickListener{
            val intent = Intent(this, FilterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}