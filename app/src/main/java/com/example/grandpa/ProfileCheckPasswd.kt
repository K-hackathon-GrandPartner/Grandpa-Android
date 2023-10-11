package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ProfileCheckPasswd:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_check_passwd)

        val checkBtn = findViewById<Button>(R.id.profile_btn)

        checkBtn.setOnClickListener {
            val intent = Intent(this, ProfilePhoneOrPasswd::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
            finish()
        }

        val backbtn = findViewById<ImageButton>(R.id.profile_back3)

        backbtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}