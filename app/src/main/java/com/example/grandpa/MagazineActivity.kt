package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MagazineActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.magazine)

        val searchImageView: ImageView = findViewById(R.id.magazine_search)
        searchImageView.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish()
        }

        val heartImageView: ImageView = findViewById(R.id.magazine_heart)
        heartImageView.setOnClickListener {
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            finish()
        }

        val checkImageView: ImageView = findViewById(R.id.magazine_check)
        checkImageView.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profileImageView: ImageView = findViewById(R.id.magazine_profile)
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}