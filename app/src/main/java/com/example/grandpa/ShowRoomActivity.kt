package com.example.grandpa

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShowRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showroomlist)

        // 더미 데이터 리스트 예시
        val roomList = arrayListOf(
            room_data(R.drawable.home, "아파트", "큰 방", 5,7,
                100,30, "광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,7,
                100,30, "광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,7,
                100,30, "광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,7,
                100,30, "광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,7,
                100,30, "광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,7,
                100,30, "광진구","건국대 도보 5분"),
            room_data(R.drawable.home, "아파트", "큰 방", 5,7,
                100,30, "광진구","건국대 도보 5분"),
        )

        val rv_room = findViewById<RecyclerView>(R.id.room_list)
        val sumOfRoom = findViewById<TextView>(R.id.CountRoom)

        sumOfRoom.text = "총 ${roomList.size} 개"

        rv_room.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_room.setHasFixedSize(true)
        rv_room.adapter = ShowRoomAdapter(roomList)



    }
}