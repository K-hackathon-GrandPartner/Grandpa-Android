package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HeartActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.heart_room)

        val searchImageView: ImageView = findViewById(R.id.heart_search)
        searchImageView.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish()
        }

        val checkImageView: ImageView = findViewById(R.id.heart_check)
        checkImageView.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            finish()
        }

        val chatImageView: ImageView = findViewById(R.id.heart_chat)
        chatImageView.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profileImageView: ImageView = findViewById(R.id.heart_profile)
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}