package com.example.grandpa

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.grandpa.databinding.ReviewPopBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailReviewPopDialog(
    context: Context,
    private val landlordId: Int,
    private val okCallback: (String) -> Unit,
) : Dialog(context) { // 뷰를 띄워야하므로 Dialog 클래스는 context를 인자로 받는다.

    private lateinit var binding: ReviewPopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReviewPopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProfileMoreData() { responseData ->
            Log.d("getProfileData", responseData.toString())
            initViews(responseData)
        }

    }

    private fun initViews(data : landlordDetailProfile) = with(binding) {
        // 빈 화면 터치를 통해 dialog가 사라지지 않도록
        setCancelable(false)

        // 임대인 프로필
        Glide.with(getRoot().getContext())
            .load(data.profileImageUrl)
            .circleCrop()
            .into(reviewTopImage)

        reviewTopName.text = data.name
        reviewTopcontent.text = data.introduction
        reviewTopstaravg.text = data.rating.toString()
        reviewTopstarcnt.text = " (" + data.reviewCount + "개)"
        setStar(data.rating, "reviewTopstar")

        Glide.with(getRoot().getContext())
            .load(data.review.profileImageUrl)
        reviewUnderName.text = data.review.name
        reviewUnderavg.text = data.review.rating.toString()
        setStar(data.review.rating, "reviewUnderstar")
        reviewUndercontent.text = data.review.content



        reviewBack.setOnClickListener {
            dismiss()
        }


    }

    fun setStar(rating: Float, offDefaultName: String){
        val bindingClass = binding::class.java

        val ratingIntPart = rating.toInt() // 정수 부분
        val ratingDecimalPart = rating - ratingIntPart // 소수 부분

        // fullstar 이미지 설정
        for (i in 1..ratingIntPart) {
            val offName = offDefaultName + i.toString()
            try {
                val field = bindingClass.getDeclaredField(offName)
                field.isAccessible = true
                val offView = field.get(binding) as ImageView
                offView.setImageResource(R.drawable.fullstar)
            } catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        // halffullstar 이미지 설정 (소수 부분이 0 이상일 때)
        if (ratingDecimalPart > 0) {
            val offName = offDefaultName + (ratingIntPart + 1).toString()
            try {
                val field = bindingClass.getDeclaredField(offName)
                field.isAccessible = true
                val offView = field.get(binding) as ImageView
                offView.setImageResource(R.drawable.halffullstar)
            } catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }
    }

    fun getProfileMoreData(callback: (landlordDetailProfile) -> Unit){
        val service = DetailProfileImpl.service_ct_tab

        val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance()
        val token = LoginTokenData.getString("accessToken", null)

        val url = USER_URL + landlordId +"/"
        val call = service.getDetailProfileData(url, token.toString())

        call.enqueue(object : Callback<DetailProfileResponse> {
            override fun onResponse(
                call: Call<DetailProfileResponse>,
                response: Response<DetailProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val responseData = apiResponse.result
                        Log.d("임대인 데이터 조회", responseData.toString())
                        callback(responseData)
                    }
                }else {
                    // 서버 응답 실패 처리
                    Log.e("Response", "Response is not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DetailProfileResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("Response", "Network error: ${t.message}")
            }

        })
    }
}