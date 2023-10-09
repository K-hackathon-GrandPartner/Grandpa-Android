package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class ProfilePhoneOrPasswd:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_phone_or_passwd)

        val phone = findViewById<ConstraintLayout>(R.id.select_phone)
        val passwd = findViewById<ConstraintLayout>(R.id.select_passwd)

        phone.setOnClickListener {
            val intent = Intent(this, ProfileModifyPhone::class.java)
            startActivity(intent)
            finish()
        }

        passwd.setOnClickListener {
            val intent = Intent(this, ProfileModifyPasswd::class.java)
            startActivity(intent)
            finish()
        }

    }
}