package com.example.grandpa

import android.annotation.SuppressLint
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

    private lateinit var dayRecyclerView: RecyclerView
    private lateinit var weekRecyclerView: RecyclerView
    private lateinit var etcRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check)

        dayRecyclerView = findViewById(R.id.day_recyclerView)
        weekRecyclerView = findViewById(R.id.week_recyclerView)
        etcRecyclerView = findViewById(R.id.rule_recyclerView)

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
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<GetCheckResponse>, response: Response<GetCheckResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if(apiResponse != null){
                        val responseData = apiResponse.result
                        Log.d("responseData","$responseData")

                        val dayList = responseData.day
                        val weekList = responseData.week
//                        val ruleList = responseData.etc

                        val dayAdapter = CheckAdapter(dayList)
                        val weekAdapter = CheckAdapter(weekList)
//                        val etcAdapter = ruleList?.let { CheckAdapter(it) }

                        dayRecyclerView.apply {
                            layoutManager = LinearLayoutManager(this@CheckActivity,LinearLayoutManager.VERTICAL,false)
                            adapter = dayAdapter
                        }

                        weekRecyclerView.apply {
                            layoutManager = LinearLayoutManager(this@CheckActivity,LinearLayoutManager.VERTICAL,false)
                            adapter = weekAdapter
                        }

//                        etcRecyclerView.apply {
//                            layoutManager = LinearLayoutManager(this@CheckActivity,LinearLayoutManager.VERTICAL,false)
//                            adapter = etcAdapter
//                        }

                        val dayAllCheck = findViewById<TextView>(R.id.day_all_check)
                        val weekAllCheck = findViewById<TextView>(R.id.week_all_check)

                        dayAllCheck.setOnClickListener {
                            handleAllCheckClick(dayAllCheck, dayAdapter)
                        }

                        weekAllCheck.setOnClickListener {
                            handleAllCheckClick(weekAllCheck, weekAdapter)
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


    @SuppressLint("NotifyDataSetChanged")
    private fun handleAllCheckClick(allCheckTextView: TextView, adapter: CheckAdapter) {
        if (!adapter.isAnyChecked()) {
            // 어떤 체크박스도 선택되어 있지 않으면 모두 선택
            adapter.checkAllCheckboxes()
            allCheckTextView.text = "전체 해제"
        } else {
            // 적어도 하나의 체크박스가 선택되어 있다면 모두 해제
            adapter.uncheckAllCheckboxes()
            allCheckTextView.text = "전체 선택"
        }

        // RecyclerView 갱신
        adapter.notifyDataSetChanged()
    }

}
