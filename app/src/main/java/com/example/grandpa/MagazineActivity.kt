package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MagazineActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.magazine)

        val searchImageView: ImageView = findViewById(R.id.magazine_search)
        searchImageView.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val heartImageView: ImageView = findViewById(R.id.magazine_heart)
        heartImageView.setOnClickListener {
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val checkImageView: ImageView = findViewById(R.id.magazine_check)
        checkImageView.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val profileImageView: ImageView = findViewById(R.id.magazine_profile)
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance()
        val token = LoginTokenData.getString("accessToken", null)

        val call = MagazineImpl.service_ct_tab.getMagazineData(token.toString())
        call.enqueue(object : Callback<MagazineResponse> {
            override fun onResponse(call: Call<MagazineResponse>, response: Response<MagazineResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if(apiResponse != null){
                        val responseData = apiResponse.result
                        Log.d("responseData","$responseData")

                        val roomImagePager: ViewPager2 = findViewById(R.id.viewPager2)
                        roomImagePager.adapter = MagazineImageAdapter(responseData, this@MagazineActivity)
                        roomImagePager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                        roomImagePager.offscreenPageLimit = 3
                        roomImagePager.currentItem = 1000

                        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
                        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()

                        roomImagePager.setPageTransformer { page, position ->
                            val myOffset = position * -(2 * pageOffset + pageMargin)
                            if (position < -1) {
                                page.translationX = -myOffset
                            } else if (position <= 1) {
                                val scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f))
                                page.translationX = myOffset
                                page.scaleY = scaleFactor
                                page.alpha = scaleFactor
                            } else {
                                page.alpha = 0f
                                page.translationX = myOffset
                            }
                        }

                    }
                } else {
                    Log.e("Magazine Response", "서버 응답 실패: ${response.body()}")
                }
            }
            override fun onFailure(call: Call<MagazineResponse>, t: Throwable) {
                Log.e("Magazine Response", "네트워크 오류: ${t.message}")
            }
        })

    }
}