package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class CheckActivity: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CheckAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check)

        getCheckListData()

        val searchImageView: ImageView = findViewById(R.id.check_search)
        searchImageView.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish()
        }

        val heartImageView: ImageView = findViewById(R.id.check_heart)
        heartImageView.setOnClickListener {
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            finish()
        }

        val chatImageView: ImageView = findViewById(R.id.check_magazine)
        chatImageView.setOnClickListener {
            val intent = Intent(this, MagazineActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profileImageView: ImageView = findViewById(R.id.check_profile)
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun getCheckListData() {
        // 서비스 객체 생성
        val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance()
        val token = LoginTokenData.getString("accessToken", null)

        val call = CheckListDataImpl.service_ct_tab.getChecklistData(token.toString())

        call.enqueue(object : Callback<GetCheckResponse> {
            override fun onResponse(call: Call<GetCheckResponse>, response: Response<GetCheckResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if(apiResponse != null){
                        val responseData = apiResponse.result
                        Log.d("responseData","$responseData")

                        val dayRecyclerView: RecyclerView = findViewById(R.id.day_recyclerView)
                        val weekRecyclerView: RecyclerView = findViewById(R.id.week_recyclerView)
                        val etcRecyclerView: RecyclerView = findViewById(R.id.rule_recyclerView)

                        val dayList = responseData.day
                        val weekList = responseData.week
                        val ruleList = responseData.etc

                        val dayAdapter = dayList?.let { CheckAdapter(it) }
                        val weekAdapter = weekList?.let { CheckAdapter(it) }
                        val etcAdapter = ruleList?.let { CheckAdapter(it) }

                        dayRecyclerView.apply {
                            layoutManager = LinearLayoutManager(this@CheckActivity,LinearLayoutManager.VERTICAL,false)
                            adapter = dayAdapter
                        }

                        weekRecyclerView.apply {
                            layoutManager = LinearLayoutManager(this@CheckActivity,LinearLayoutManager.VERTICAL,false)
                            adapter = weekAdapter
                        }

                        etcRecyclerView.apply {
                            layoutManager = LinearLayoutManager(this@CheckActivity,LinearLayoutManager.VERTICAL,false)
                            adapter = etcAdapter
                        }

                    }
                } else {
                    Log.e("Response", "서버 응답 실패: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<GetCheckResponse>, t: Throwable) {
                Log.e("Response", "네트워크 오류: ${t.message}")
            }
        })

    }
}
