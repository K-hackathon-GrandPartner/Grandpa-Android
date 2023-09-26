package com.example.grandpa

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SignupWithKakaoActivity:AppCompatActivity() {

    //회원가입 정보 저장할 db
    object SignUpInfoDB {
        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences("SignUpInfo", Context.MODE_PRIVATE)
        }

        fun getInstance(): SharedPreferences {
            if (!this::sharedPreferences.isInitialized) {
                throw IllegalStateException("SharedPreferencesSingleton is not initialized")
            }
            return sharedPreferences
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_withkakao)

        SignUpInfoDB.init(this)
        val InfoData = SignUpInfoDB.getInstance().edit()

        val kakaoInfo = intent.getSerializableExtra("kakaoInfo") as? UserBigInfo

        val backbtn: ImageView = findViewById(R.id.signup_backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val nextbtn: AppCompatButton = findViewById(R.id.signup_nextBtn)
        nextbtn.setOnClickListener {
            InfoData.apply()
            val intent = Intent(this, SignupProfileActivity::class.java)
            intent.putExtra("kakaoInfo", kakaoInfo)
            startActivity(intent)
            finish()
        }

        val name = findViewById<TextView>(R.id.signup_inputName)
        val genderWoman = findViewById<ToggleButton>(R.id.signup_genderWoman)
        val genderMan = findViewById<ToggleButton>(R.id.signup_genderMan)
        val phone = findViewById<TextView>(R.id.signup_inputPhone)
        val year = findViewById<TextView>(R.id.signup_inputYear)
        val month = findViewById<TextView>(R.id.signup_inputMonth)
        val day = findViewById<TextView>(R.id.signup_inputDay)

        //카카오에서 받아온 정보 자동 입력
        if (kakaoInfo != null) {
            name.text = kakaoInfo.nickname
            if (kakaoInfo.gender == "남성") {
                genderMan.isChecked = true
                InfoData.putString("sex", "남자")
            } else {
                genderWoman.isChecked = true
                InfoData.putString("sex", "여자")
            }

            //db에 저장
            InfoData.putLong("externalId", kakaoInfo.externalId)
            InfoData.putString("loginType", "kakao")
            InfoData.putString("name", kakaoInfo.nickname)

        } else {
            Log.e("kakaoInfo 오류", "null")
        }

        phone.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val phoneText = phone.text.toString()
                if (phoneText.isNotBlank()) {
                    val phoneNum = formatPhoneNumber(phoneText)
                    Log.d("phoneNum", phoneNum)
                    InfoData.putString("cellPhone", phoneNum)
                } else {
                    Log.e("InfoData", "phone is null or empty")
                }
            }
        }

        val signupCheckBox = findViewById<CheckBox>(R.id.signup_checkService)

        signupCheckBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                val yearText = year.text.toString()
                val monthText = month.text.toString()
                val dayText = day.text.toString()
                if (yearText.isNotBlank() && monthText.isNotBlank() && dayText.isNotBlank()) {
                    val birth = formatBirth(yearText, monthText, dayText)
                    Log.d("birth", birth)
                    InfoData.putString("birth",birth)
                } else {
                    Log.e("InfoData", "year, month, or day is null")
                }
            }
        }
    }

    //폰번호 정규식 표현법으로 변환하는 함수
    private fun formatPhoneNumber(phoneNumber: String): String {
        if (phoneNumber.length != 11) {
            Toast.makeText(this, "전화번호를 다시 입력하세요", Toast.LENGTH_SHORT).show()
            // 입력된 전화번호가 11자리가 아니면 알림
        }
        return "${phoneNumber.substring(0, 3)}-${phoneNumber.substring(3, 7)}-${phoneNumber.substring(7)}"
    }

    //생년월일 합치는 함수
    private fun formatBirth(year: String, month: String, day: String): String {
        return "${year.substring(0,4)}-${month.substring(0,2)}-${day.substring(0,2)}"
    }
}