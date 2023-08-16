package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ChatActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)

        val searchImageView: ImageView = findViewById(R.id.search)
        searchImageView.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish()
        }

        val heartImageView: ImageView = findViewById(R.id.heart)
        heartImageView.setOnClickListener {
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            finish()
        }

        val checkImageView: ImageView = findViewById(R.id.check)
        checkImageView.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profileImageView: ImageView = findViewById(R.id.profile)
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}