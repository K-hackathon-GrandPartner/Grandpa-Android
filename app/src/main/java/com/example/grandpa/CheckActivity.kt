package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CheckActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check)

        val searchImageView: ImageView = findViewById(R.id.check_search)
        searchImageView.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish()
        }

        val heartImageView: ImageView = findViewById(R.id.check_heart)
        heartImageView.setOnClickListener {
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            finish()
        }

        val chatImageView: ImageView = findViewById(R.id.check_magazine)
        chatImageView.setOnClickListener {
            val intent = Intent(this, MagazineActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profileImageView: ImageView = findViewById(R.id.check_profile)
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}