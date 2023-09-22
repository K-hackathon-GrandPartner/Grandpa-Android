package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SignupWithKakaoActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_withkakao)

        val backbtn: ImageView = findViewById(R.id.signup_backbtn)
        backbtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val nextbtn: AppCompatButton = findViewById(R.id.signup_nextBtn)
        nextbtn.setOnClickListener{
            val intent = Intent(this, SignupProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val kakaoInfo = intent.getSerializableExtra("kakaoInfo") as? UserBigInfo
        if (kakaoInfo != null) {
            Log.d("kakaoInfo", kakaoInfo.toString())
        }
    }
}