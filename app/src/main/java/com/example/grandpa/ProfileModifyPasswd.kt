package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ProfileModifyPasswd:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_modify_passwd)

        val backbtn = findViewById<ImageButton>(R.id.profile_back3)

        backbtn.setOnClickListener {
            val intent = Intent(this, ProfilePhoneOrPasswd::class.java)
            startActivity(intent)
            finish()
        }

    }
}