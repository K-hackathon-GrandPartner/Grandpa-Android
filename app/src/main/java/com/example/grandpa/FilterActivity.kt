package com.example.grandpa
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_roominfo) // filter_roomInfo.xml 파일과 연결

//        val sumOfRoom = findViewById<TextView>(R.id.CountRoom)
//
//        sumOfRoom.text = "총 ${ShowRoomActivity.} 개"
    }
}