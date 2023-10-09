package com.example.grandpa

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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

        val name = findViewById<TextView>(R.id.signup_inputName)
        val genderWoman = findViewById<ToggleButton>(R.id.signup_genderWoman)
        val genderMan = findViewById<ToggleButton>(R.id.signup_genderMan)
        val phone = findViewById<TextView>(R.id.signup_inputPhone)
        val year = findViewById<TextView>(R.id.signup_inputYear)
        val month = findViewById<TextView>(R.id.signup_inputMonth)
        val day = findViewById<TextView>(R.id.signup_inputDay)
        val signupCheckBox = findViewById<CheckBox>(R.id.signup_checkService)
        val signupCheckBox2 = findViewById<CheckBox>(R.id.signup_checkPersonalInfo)

        val backbtn: ImageView = findViewById(R.id.signup_backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
            finish()
        }

        val nextbtn: AppCompatButton = findViewById(R.id.signup_nextBtn)
        nextbtn.setOnClickListener {
            if(signupCheckBox.isChecked && signupCheckBox2.isChecked){
                InfoData.apply() //db저장
                val intent = Intent(this, SignupProfileActivity::class.java)
                overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
                intent.putExtra("kakaoInfo", kakaoInfo)
                startActivity(intent)
                finish()
            } else{
                Toast.makeText(this,"필수동의 항목을 체크해주세요", LENGTH_SHORT).show()
            }
        }

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


        // 글자수 제한 설정
        val phoneMaxLength = 11
        val phoneInputFilter = InputFilter.LengthFilter(phoneMaxLength)
        phone.filters = arrayOf(phoneInputFilter)

        phone.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val phoneText = phone.text.toString()
                if (phoneText.isNotBlank()) {
                    if(chkNum(phoneText)){
                        val phoneNum = formatPhoneNumber(phoneText)
                        if(phoneNum == "retry"){
                            Toast.makeText(this,"전화번호를 다시 입력하세요",LENGTH_SHORT).show()
                        } else{
                            Log.d("phoneNum", phoneNum)
                            InfoData.putString("cellPhone", phoneNum)
                        }
                    } else{
                        Toast.makeText(this,"-없이 다시 입력해주세요",LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("InfoData", "phone is null or empty")
                }
            }
        }

        // 글자수 제한 설정
        val yearMaxLength = 4
        val yearInputFilter = InputFilter.LengthFilter(yearMaxLength)
        year.filters = arrayOf(yearInputFilter)

        val monthMaxLength = 2
        val monthInputFilter = InputFilter.LengthFilter(monthMaxLength)
        month.filters = arrayOf(monthInputFilter)

        val dayMaxLength = 2
        val dayInputFilter = InputFilter.LengthFilter(dayMaxLength)
        day.filters = arrayOf(dayInputFilter)

        year.setOnClickListener{
            val phoneText = phone.text.toString()
            if(phoneText.isEmpty()){
                Toast.makeText(this,"전화번호를 입력하세요", LENGTH_SHORT).show()
            }
        }

        month.setOnClickListener{
            val monthText = month.text.toString()
            if(monthText.length != 4){
                Toast.makeText(this,"년도를 입력하세요", LENGTH_SHORT).show()
            }
        }



        signupCheckBox.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                val yearText = year.text.toString()
                val monthText = month.text.toString()
                val dayText = day.text.toString()
                if (yearText.isNotBlank() && monthText.isNotBlank() && dayText.isNotBlank()) {
                    val birth = formatBirth(yearText, monthText, dayText)
                    Log.d("birth", birth)
                    InfoData.putString("birth",birth)
                } else {
                    Toast.makeText(this,"생년월일을 입력하세요", LENGTH_SHORT).show()
                    Log.e("InfoData", "year, month, or day is null")
                }
            }
        }
    }


    //생년월일 합치는 함수
    private fun formatBirth(year: String, month: String, day: String): String {
        return "${year}-${month}-${day}"
    }

    //폰번호 정규식 표현법으로 변환하는 함수
    private fun formatPhoneNumber(phoneNumber:String): String {
        return if(phoneNumber.length == 10){
            "${phoneNumber.substring(0, 3)}-${phoneNumber.substring(3, 6)}-${phoneNumber.substring(6)}"
        } else if(phoneNumber.length == 11){
            "${phoneNumber.substring(0, 3)}-${phoneNumber.substring(3, 7)}-${phoneNumber.substring(7)}"
        } else{
            "retry"
        }
    }


    //정수인지 확인하는 함수
    private fun chkNum(str: String) : Boolean {
        var temp: Char
        var result = true

        for (i in str.indices) {
            temp = str.elementAt(i)
            if (temp.code < 48 || temp.code > 57) {
                result = false
            }
        }
        return result
    }

}