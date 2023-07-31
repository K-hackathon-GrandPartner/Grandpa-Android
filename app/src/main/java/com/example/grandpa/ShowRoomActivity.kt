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

        val recyclerView = findViewById<RecyclerView>(R.id.reRoom)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 더미 데이터 리스트 예시
        val data = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5","Item6", "Item7")

        val recyclerViewAdapter = ShowRoomAdapter(data)
        recyclerView.adapter = recyclerViewAdapter

        val countTextView = findViewById<TextView>(R.id.CountText)
        countTextView.text = "총 ${data.size} 개"
    }
}