package com.example.grandpa

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.example.grandpa.databinding.DetailInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        // API 요청
        val callUrl = DetailRoomImpl.BASE_URL + roomId.toString() + "/"
        val call = service.requestList(callUrl)

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
        if(rule.curfew == 1){
            binding.detailInfoRule1.text = "몇시 이내 귀가"
        }else{
            binding.detailInfoRule1.text = "상관 없음"
        }

        if(rule.smoking == 1){
            binding.detailInfoRule2.text = "비흡연"
        }else{
            binding.detailInfoRule2.text = "상관 없음"
        }

        if(rule.drinking == 1){
            binding.detailInfoRule3.text = "음주"
        }else{
            binding.detailInfoRule3.text = "상관 없음"
        }

        if(rule.religion == 0){
            binding.detailInfoRule4.text = "없음"
        }
    }

    fun setCareServiceLayout(care : List<String>){
        val defaultName : String = "detailInfo_careService"
        var setName : String
        val bindingClass = binding::class.java

        for(i in 1..5){
            setName = defaultName + i.toString()

            if(care.size>i){
                try{
                    val field = bindingClass.getDeclaredField(setName)
                    field.isAccessible = true
                    val offView = field.get(binding) as TextView
                    offView.isGone = true
                }catch (e: NoSuchFieldException) {
                    // Handle the case where the field doesn't exist
                } catch (e: IllegalAccessException) {
                    // Handle any access-related exceptions
                }

            }else{
                try{
                    val field = bindingClass.getDeclaredField(setName)
                    field.isAccessible = true
                    val onView = field.get(binding) as TextView
                    onView.text = care[i-1]
                } catch (e: NoSuchFieldException) {
                    // Handle the case where the field doesn't exist
                } catch (e: IllegalAccessException) {
                    // Handle any access-related exceptions
                }

            }
        }
    }

    fun setSafetyLayout(safety: Safety){
        if(safety.cctv== 1){
            binding.detailInfoSafety1.text = "CCTV 있음"
        }else{
            binding.detailInfoSafety1.text = "CCTV 없음"
        }

        if(safety.fireExtinguisher== 1){
            binding.detailInfoSafety2.text = "소화기 있음"
        }else{
            binding.detailInfoSafety2.text = "소화기 없음"
        }

        if(safety.firstAidKit== 1){
            binding.detailInfoSafety3.text = "구급 상자 있음"
        }else{
            binding.detailInfoSafety3.text = "구급 상자 없음"
        }

        if(safety.fireAlarm== 1){
            binding.detailInfoSafety4.text = "화재 경보기 있음"
        }else{
            binding.detailInfoSafety4.text = "화재 경보기 없음"
        }

        if(safety.carbonMonoxideAlarm== 1){
            binding.detailInfoSafety5.text = "일산화탄소 경보기 있음"
        }else{
            binding.detailInfoSafety5.text = "일산화탄소 경보기 없음"
        }
    }

    fun setPetLayout(pet : Pet){
        if(pet.dog == 1){
            binding.detailInfoDog.text = "개 있음"
        }else{
            binding.detailInfoDog.isGone= true
        }

        if(pet.cat == 1){
            binding.detailInfoCat.text = "고양이 있음"
        }else{
            binding.detailInfoCat.isGone = true
        }

        if(pet.etc == 1){
            binding.detailInfoEtc.text = "기타"
        }else{
            binding.detailInfoEtc.isGone = true
        }

    }
}