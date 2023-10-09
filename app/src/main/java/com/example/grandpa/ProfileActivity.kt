package com.example.grandpa

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.grandpa.databinding.ProfileUploadSchoolBinding
import org.w3c.dom.Text

class ProfileActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile)

        val searchImageView: ImageView = findViewById(R.id.profile_search)
        searchImageView.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val heartImageView: ImageView = findViewById(R.id.profile_heart)
        heartImageView.setOnClickListener {
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val checkImageView: ImageView = findViewById(R.id.profile_check)
        checkImageView.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val chatImageView: ImageView = findViewById(R.id.profile_magazine)
        chatImageView.setOnClickListener {
            val intent = Intent(this, MagazineActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val modifyInfo = findViewById<TextView>(R.id.profile_modifyInfo)
        val modifyPhonePasswd = findViewById<TextView>(R.id.profile_modifyPhonePasswd)
        val modifyUpload = findViewById<TextView>(R.id.pprofile_upload)
        val logout = findViewById<TextView>(R.id.profile_logout)

        modifyInfo.setOnClickListener{
            val intent = Intent(this, ProfileModifyInfo::class.java)
            startActivity((intent))
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
            finish()
        }

        modifyPhonePasswd.setOnClickListener{
            val intent = Intent(this, ProfileCheckPasswd::class.java)
            startActivity((intent))
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
            finish()
        }

        modifyUpload.setOnClickListener{
            val intent = Intent(this, ProfileUploadSchool::class.java)
            startActivity((intent))
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
            finish()
        }

    }
}