package com.example.grandpa

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide

class SignupProfileActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_profile)

        SignupWithKakaoActivity.SignUpInfoDB.init(this)
        val InfoData = SignupWithKakaoActivity.SignUpInfoDB.getInstance().edit()

        val backbtn: ImageView = findViewById(R.id.signup_profile_back)
        backbtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profileImg = findViewById<ImageView>(R.id.signup_Image)
        val kakaoInfo = intent.getSerializableExtra("kakaoInfo") as? UserBigInfo
        val imgUrl = kakaoInfo?.profileImage
        val defaultImage = R.drawable.signprofile
        val protestant = findViewById<ToggleButton>(R.id.signup_protestant)
        val buddhism = findViewById<ToggleButton>(R.id.signup_buddhism)
        val catholic = findViewById<ToggleButton>(R.id.signup_catholic)
        val none = findViewById<ToggleButton>(R.id.signup_noreligion)

        val nextbtn: AppCompatButton = findViewById(R.id.signup_nextBtn2)
        nextbtn.setOnClickListener {
            if (kakaoInfo != null) {
                InfoData.putString("profileImage",kakaoInfo.profileImage)
            }

            var checkedCount = 0

            if (protestant.isChecked) checkedCount++
            if (buddhism.isChecked) checkedCount++
            if (catholic.isChecked) checkedCount++
            if (none.isChecked) checkedCount++

            if(checkedCount > 1){
                Toast.makeText(this,"종교를 하나만 선택하세요",LENGTH_SHORT).show()
            } else{
                SaveIntroduction()
                InfoData.apply()
                val intent = Intent(this, SignupLocationSchoolActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        Glide.with(this)
            .load(imgUrl) // 불러올 이미지 url
            .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .circleCrop() // 동그랗게 자르기
            .into(profileImg) // 이미지를 넣을 뷰


        protestant.setOnCheckedChangeListener { buttonView, isChecked ->
            if(protestant.isChecked){
                val text = protestant.text
                InfoData.putString("religion", text.toString())
            }else{
                InfoData.remove("religion")
            }
        }

        buddhism.setOnCheckedChangeListener { buttonView, isChecked ->
            if(buddhism.isChecked){
                val text = buddhism.text
                InfoData.putString("religion", text.toString())
            }else{
                InfoData.remove("religion")
            }
        }

        catholic.setOnCheckedChangeListener { buttonView, isChecked ->
            if(catholic.isChecked){
                val text = catholic.text
                InfoData.putString("religion", text.toString())
            }else{
                InfoData.remove("religion")
            }
        }

        none.setOnCheckedChangeListener { buttonView, isChecked ->
            if(none.isChecked){
                val text = none.text
                InfoData.putString("religion", text.toString())
            }else{
                InfoData.remove("religion")
            }
        }

    }

    //자기소개 내용 db에 저장하는 함수
    @SuppressLint("CommitPrefEdits")
    private fun SaveIntroduction() {
        SignupWithKakaoActivity.SignUpInfoDB.init(this)
        val InfoData = SignupWithKakaoActivity.SignUpInfoDB.getInstance().edit()
        val introMySelf = findViewById<EditText>(R.id.signup_inputintro)

        // 글자수 제한 설정
        val introMaxLength = 11
        val introInputFilter = InputFilter.LengthFilter(introMaxLength)
        introMySelf.filters = arrayOf(introInputFilter)

        val introText = introMySelf.text.toString()
        if (introText.isNotBlank()) {
            InfoData.putString("introduction", introText)
        } else {
            Log.e("introData", "Introduction is null or empty")
        }
    }
}