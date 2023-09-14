package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SignupLocationSchoolActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_loactionschool)

        val backbtn: ImageView = findViewById(R.id.signup_locationschool_back)
        backbtn.setOnClickListener{
            val intent = Intent(this, SignupProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val nextbtn: AppCompatButton = findViewById(R.id.signup_nextBtn3)
        nextbtn.setOnClickListener{
            val intent = Intent(this, SignupEndActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}