package com.example.grandpa

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val signupText: TextView = findViewById(R.id.login_signUp)
        signupText.setOnClickListener {
            val intent = Intent(this, SignupWithPhoneActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}