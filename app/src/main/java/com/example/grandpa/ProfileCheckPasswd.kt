package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProfileCheckPasswd:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_check_passwd)

        val nextBtn = findViewById<Button>(R.id.profile_btn)

        nextBtn.setOnClickListener {
            val intent = Intent(this, ProfilePhoneOrPasswd::class.java)
            startActivity(intent)
            finish()
        }

    }
}