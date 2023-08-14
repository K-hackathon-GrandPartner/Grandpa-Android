package com.example.grandpa
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_roominfo) // filter_roomInfo.xml 파일과 연결

        //roomlist 개수 가져옴
        val roomListSize = intent.getIntExtra("roomListSize", 0) //0은 기본 값
        val sumOfRoom = findViewById<TextView>(R.id.CountRoom)
        sumOfRoom.text = "총 ${roomListSize} 개"

        //Back 버튼
        val backImageView : ImageView = findViewById(R.id.filter_backbtn)
        backImageView.setOnClickListener{
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}