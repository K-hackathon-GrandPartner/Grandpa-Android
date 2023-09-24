package com.example.grandpa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.MutableMap

class SignupLocationSchoolActivity:AppCompatActivity() {
    //회원가입 엑세스 토큰 db
    object LoginTokenDB {
        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
        }

        fun getInstance(): SharedPreferences {
            if (!this::sharedPreferences.isInitialized) {
                throw IllegalStateException("SharedPreferencesSingleton is not initialized")
            }
            return sharedPreferences
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_loactionschool)

        SignupWithKakaoActivity.SignUpInfoDB.init(this)
        val InfoDB = SignupWithKakaoActivity.SignUpInfoDB.getInstance().edit()

        val backbtn: ImageView = findViewById(R.id.signup_locationschool_back)
        backbtn.setOnClickListener{
            val intent = Intent(this, SignupProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val nextbtn: AppCompatButton = findViewById(R.id.signup_nextBtn3)
        nextbtn.setOnClickListener{
            SignUpDataToServer()
            val intent = Intent(this, SignupEndActivity::class.java)
            startActivity(intent)
            finish()
        }


        val regionData = arrayOf("구", "광진구", "노원구", "성북구")
        val Gwangjin_schoolData = arrayOf("건국대학교", "세종대학교", "장로회신대학교")
        val Nowon_schoolData = arrayOf("광운대학교", "서울과학기술대학교", "서울여자대학교", "인덕대학교", "한국성서대학교", "삼육대학교")
        val Seongbuk_schoolData = arrayOf("고려대학교", "한성대학교", "서경대학교", "한국예술종합학교", "동덕여자대학교", "성신여자대학교", "국민대학교")
        val regionSpinner = findViewById<Spinner>(R.id.spinner_location)
        val schoolSpinner = findViewById<Spinner>(R.id.spinner_school)

        val regionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, regionData)
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionSpinner.adapter = regionAdapter

        // 지역 스피너 선택 이벤트 처리
        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (regionData[position]) {
                    "구" -> setSchoolSpinnerValues(schoolSpinner, arrayOf("구를 선택하세요"))
                    "광진구" -> setSchoolSpinnerValues(schoolSpinner, Gwangjin_schoolData)
                    "노원구" -> setSchoolSpinnerValues(schoolSpinner, Nowon_schoolData)
                    "성북구" -> setSchoolSpinnerValues(schoolSpinner, Seongbuk_schoolData)
                }

                val selectedRegion = regionSpinner.getItemAtPosition(position).toString()
                InfoDB.putString("region",selectedRegion)
                InfoDB.apply()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val context: Context = applicationContext
                val text = "구, 학교를 선택하세요"
                val duration = Toast.LENGTH_SHORT
                Toast.makeText(context, text, duration).show()
                //+선택안하면 다음 버튼 눌려도 못넘어가도록
            }
        }

        //학교 스피너 이벤트처리
        schoolSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSchool = schoolSpinner.getItemAtPosition(position).toString()
                InfoDB.putString("university",selectedSchool)
                InfoDB.apply()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val context: Context = applicationContext
                val text = "구, 학교를 선택하세요"
                val duration = Toast.LENGTH_SHORT
                Toast.makeText(context, text, duration).show()
                //+선택안하면 다음 버튼 눌려도 못넘어가도록
            }
        }
    }

    //학교 스피너 값을 설정하는 함수
    private fun setSchoolSpinnerValues(spinner: Spinner, values: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    //db에 저장되어 있는 데이터들 꺼내오기
    private fun dataToSend(): HashMap<String, Any> {
        SignupWithKakaoActivity.SignUpInfoDB.init(this)
        val InfoDB = SignupWithKakaoActivity.SignUpInfoDB.getInstance()
        val paramsMap = HashMap<String, Any>()
        val keys = InfoDB.all.keys

        for (key in keys) {
            val value = InfoDB.all[key]
            if (value != null) {
                paramsMap[key] = value
            }
        }
        return paramsMap
    }

    private fun SignUpDataToServer() {
        val service = AuthKakaoSignUpImpl.service_ct_tab
        val requestData = dataToSend() // 요청할 데이터 설정
        Log.d("requsetData", requestData.toString())
        val call = service.postSignUpInfo(requestData) //post 함

        var accessTokenInfo : SignUpToken? = null
        LoginTokenDB.init(this)
        val LoginTokenData = LoginTokenDB.getInstance().edit()

        call.enqueue(object : Callback<SignUpToken> {
            override fun onResponse(call: Call<SignUpToken>, response: Response<SignUpToken>) {
                val statusCode = response.code() // 응답의 상태 코드를 가져옴

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null){
                        accessTokenInfo = responseBody
                        //Log.d("accessToken", accessToken.toString())
                        val accessToken = (accessTokenInfo as SignUpToken).result.accessToken.toString()
                        Log.d("accessToken", accessToken)
                        LoginTokenData.putString("accessToken", "Bearer " + accessToken)
                        LoginTokenData.apply()
                    }
                } else {
                    // 서버가 오류 응답을 반환한 경우 처리하는 코드 추가
                    Log.d("error", "서버가 오류 응답 반환, 상태 코드: $statusCode")
                }
            }
            override fun onFailure(call: Call<SignUpToken>, t: Throwable) {
                // 네트워크 오류 발생 시 처리하는 코드 추가
                Log.e("Response2", "Network error: ${t.message}")
            }
        })
    }
}