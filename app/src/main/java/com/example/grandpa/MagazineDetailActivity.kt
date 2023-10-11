package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.grandpa.databinding.MagazineDetailBinding
import com.example.grandpa.databinding.RoomDetailBinding
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MagazineDetailActivity:AppCompatActivity() {
    private val binding by lazy{
        MagazineDetailBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.magazinedetailBack.setOnClickListener {
            finish()
        }

        binding.magazinedetailSearch.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        binding.magazinedetailHeart.setOnClickListener {
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        binding.magazinedetailCheck.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        binding.magazinedetailProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance()
        val token = LoginTokenData.getString("accessToken", null)

        val magazineId = intent.getStringExtra("magazineId")
        Log.d("메거진 id", magazineId.toString())

        val url = MAGAZINE_URL + magazineId +"/"
        Log.d("pushurl", url)

        val call = MagazineDetailImpl.service_ct_tab.getMagazineDetailData(url, token.toString())
        call.enqueue(object : Callback<MagazineDetailResponse> {
            override fun onResponse(call: Call<MagazineDetailResponse>, response: Response<MagazineDetailResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if(apiResponse != null){
                        val responseData = apiResponse.result
                        Log.d("responseData","$responseData")

                        Glide.with(applicationContext).load(responseData.imageUrl).into(binding.magazinedetailImage)
                        binding.magazinedetailContent.text = responseData.content

                    }
                } else {
                    Log.e("Magazine Response", "서버 응답 실패: ${response.body()}")
                }
            }
            override fun onFailure(call: Call<MagazineDetailResponse>, t: Throwable) {
                Log.e("Magazine Response", "네트워크 오류: ${t.message}")
            }
        })
    }
}