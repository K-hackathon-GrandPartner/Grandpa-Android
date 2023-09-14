package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SignupProfileActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_profile)

        val backbtn: ImageView = findViewById(R.id.signup_profile_back)
        backbtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val nextbtn: AppCompatButton = findViewById(R.id.signup_nextBtn2)
        nextbtn.setOnClickListener{
            val intent = Intent(this, SignupLocationSchoolActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}