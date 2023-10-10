package com.example.grandpa

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class ProfilePhoneOrPasswd:AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_phone_or_passwd)

        val phone = findViewById<ConstraintLayout>(R.id.select_phone)
        val passwd = findViewById<ConstraintLayout>(R.id.select_passwd)
        val backbtn = findViewById<ImageButton>(R.id.profile_back3)
        val phoneImg = findViewById<ImageButton>(R.id.phoneImg)
        val passwdImg = findViewById<ImageButton>(R.id.passwdImg)

        phone.setOnClickListener {
            val intent = Intent(this, ProfileModifyPhone::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
            finish()
        }

        phoneImg.setOnClickListener {
            val intent = Intent(this, ProfileModifyPhone::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
            finish()
        }

        passwd.setOnClickListener {
            val intent = Intent(this, ProfileModifyPasswd::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
            finish()
        }

        passwdImg.setOnClickListener {
            val intent = Intent(this, ProfileModifyPasswd::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
            finish()
        }

        backbtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}