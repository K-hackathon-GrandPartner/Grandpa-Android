package com.example.grandpa

import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.example.grandpa.databinding.DetailInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.grandpa.BASE_URL

class DetailInfoPopActivity : AppCompatActivity() {
    private val binding by lazy{
        DetailInfoBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.detailInfoBack.setOnClickListener {
            finish()
        }

        val roomId = intent.getIntExtra("room_id", 0) //0은 기본 값

        // 서비스 객체 생성
        val service = DetailRoomImpl.service_ct_tab

        val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance()
        val token = LoginTokenData.getString("accessToken", null)

        // API 요청
        val callUrl = BASE_URL + roomId.toString() + "/"
        val call = service.requestList(callUrl, token.toString())

        call.enqueue(object: Callback<DetailRoomResponse> {
            override fun onResponse(
                call: Call<DetailRoomResponse>,
                response: Response<DetailRoomResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val roomInfo = apiResponse.result

                        setRuleLayout(roomInfo.rule)
                        binding.detailInfoRule4.text = "- "+roomInfo.rule.religion
                        setCareServiceLayout(roomInfo.careServices)
                        setSafetyLayout(roomInfo.safety)
                        setPetLayout(roomInfo.pet)
                    }

                }else {
                    // 서버 응답 실패 처리
                    Log.e("Response", "Response is not successful. Code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<DetailRoomResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("Response", "Network error: ${t.message}")
            }
        })
    }

    fun setRuleLayout(rule : Rule){
        if(rule.curfew == 0){
            binding.detailInfoRule1.text = "- 상관 없음"
        }else{
            binding.detailInfoRule1.text = "- " + rule.curfew.toString() + "시 이내 귀가"
        }

        if(rule.smoking == 0){
            binding.detailInfoRule2.text = "- 비흡연"
        }else if(rule.smoking==1){
            binding.detailInfoRule2.text = "- 흡연 가능"
        }else{
            binding.detailInfoRule2.text = "- 흡연 상관없음"
        }

        if(rule.drinking == 0){
            binding.detailInfoRule3.text = "- 금주"
        }else if(rule.drinking ==1){
            binding.detailInfoRule3.text = "- 음주 가능"
        }else{
            binding.detailInfoRule3.text = "- 음주 상관없음"
        }
    }

    fun setCareServiceLayout(care : List<String>){
        val defaultName = "detailInfoCareService"
        var setName: String
        val bindingClass = binding::class.java

        for(i in 1..5){
            Log.d("i", i.toString())
            setName = defaultName + i.toString()
            if(care.size >= i){
                try {
                    val field = bindingClass.getDeclaredField(setName)
                    field.isAccessible = true
                    val setView = field.get(binding) as TextView
                    setView.text = "- " + care[i-1]
                } catch (e: NoSuchFieldException) {
                    Log.d("NoSuchFieldException", "NoSuchFieldException")
                } catch (e: IllegalAccessException) {
                    Log.d("IllegalAccessException", "IllegalAccessException")
                }
            }else{
                try {
                    val field = bindingClass.getDeclaredField(setName)
                    field.isAccessible = true
                    val setView = field.get(binding) as TextView
                    setView.isGone = true
                } catch (e: NoSuchFieldException) {
                    Log.d("NoSuchFieldException", "NoSuchFieldException")
                } catch (e: IllegalAccessException) {
                    Log.d("IllegalAccessException", "IllegalAccessException")
                }
            }

        }
    }

    fun setSafetyLayout(safety: Safety){
        if(safety.cctv== 1){
            binding.detailInfoSafety1.text = "- CCTV 있음"
        }else{
            binding.detailInfoSafety1.text = "- CCTV 없음"
        }

        if(safety.fireExtinguisher== 1){
            binding.detailInfoSafety2.text = "- 소화기 있음"
        }else{
            binding.detailInfoSafety2.text = "소화기 없음"
        }

        if(safety.firstAidKit== 1){
            binding.detailInfoSafety3.text = "- 구급 상자 있음"
        }else{
            binding.detailInfoSafety3.text = "- 구급 상자 없음"
        }

        if(safety.fireAlarm== 1){
            binding.detailInfoSafety4.text = "- 화재 경보기 있음"
        }else{
            binding.detailInfoSafety4.text = "- 화재 경보기 없음"
        }

        if(safety.carbonMonoxideAlarm== 1){
            binding.detailInfoSafety5.text = "- 일산화탄소 경보기 있음"
        }else{
            binding.detailInfoSafety5.text = "- 일산화탄소 경보기 없음"
        }
    }

    fun setPetLayout(pet : Pet){
        if(pet.dog == 1){
            binding.detailInfoDog.text = "- 개 있음"
        }else{
            binding.detailInfoDog.isGone= true
        }

        if(pet.cat == 1){
            binding.detailInfoCat.text = "- 고양이 있음"
        }else{
            binding.detailInfoCat.isGone = true
        }

        if(pet.etc == 1){
            binding.detailInfoEtc.text = "- 기타"
        }else{
            binding.detailInfoEtc.isGone = true
        }

    }
}