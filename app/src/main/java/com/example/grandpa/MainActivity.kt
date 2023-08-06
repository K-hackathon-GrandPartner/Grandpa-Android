package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //3초 후에 다음 액태비티로 전환
        Handler().postDelayed({
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish() //이전 액티비티 종료
        },3000) //3초
    }
}