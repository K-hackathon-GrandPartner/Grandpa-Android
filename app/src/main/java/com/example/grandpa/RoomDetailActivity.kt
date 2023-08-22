package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.viewpager2.widget.ViewPager2
import com.example.grandpa.databinding.RoomDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDetailActivity: AppCompatActivity() {
    var setM2 : Boolean = false

    private val binding by lazy{
        RoomDetailBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.detailBack.setOnClickListener{
            finish()
        }

        binding.detailHeart.setOnClickListener {
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.detailCheck.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.detailChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.detailProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }


        val roomId = intent.getIntExtra("room_id", 0) //0은 기본 값
        Log.d("roomId", roomId.toString())

        // 서비스 객체 생성
        val service = DetailRoomImpl.service_ct_tab

        // API 요청
        val callUrl = DetailRoomImpl.BASE_URL + roomId.toString() + "/"
        val call = service.requestList(callUrl)

        call.enqueue(object: Callback<DetailRoomResponse>{
            override fun onResponse(
                call: Call<DetailRoomResponse>,
                response: Response<DetailRoomResponse>
            ){
                if(response.isSuccessful){
                    val apiResponse = response.body()
                    if(apiResponse != null){
                        val roomInfo = apiResponse.result
                        Log.d("responseData", roomInfo.toString())

                        //이미지 가로 전환
                        val roomImagePager: ViewPager2 = findViewById(R.id.viewPager2)
                        roomImagePager.adapter = RoomImageAdapter(roomInfo.images, this@RoomDetailActivity)
                        roomImagePager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                        binding.detailAddress.text = roomInfo.address
                        binding.detailDeposit.text = roomInfo.deposit.toString()
                        binding.detailMonthlyRent.text = roomInfo.monthlyRent.toString()
                        binding.detailRoomTitle.text = roomInfo.detail.title
                        binding.detailBuildingType.text = roomInfo.buildingType

                        val roomBuildFloor = "( " + roomInfo.roomFloor + "층 / " + roomInfo.buildingFloor + "층 )"
                        binding.detailRoomBuildingFloor.text = roomBuildFloor

                        if(setM2){
                            //true면 m2으로
                            val sizeUnit = "( " + String.format("%.1f", roomInfo.roomSize) + "㎡)"
                            binding.detailSizeUnit.text = sizeUnit

                        }else{
                            //false면 평으로
                            val sizeUnit = "( " + String.format("%.1f", roomInfo.roomSize / 3.3) + "평 )"
                            binding.detailSizeUnit.text = sizeUnit
                        }

                        binding.detailMoveInDate.text = roomInfo.moveInDate

                        val count = setOptionLayout(roomInfo.option)
                        //Log.d("count", count.toString())
                        if(count!=12) offOptionLayout(count)

                        binding.detailRule4.text = roomInfo.rule.religion

                        binding.detailM2.setOnClickListener {
                            if(setM2){
                                //true면 m2으로
                                setM2 = false
                                val sizeUnit = "( " + String.format("%.1f", roomInfo.roomSize / 3.3) + "평 )"
                                binding.detailM2.setImageResource(R.drawable.m2)
                                binding.detailSizeUnit.text = sizeUnit

                            }else{
                                //false면 평으로
                                setM2 = true
                                val sizeUnit = "( " + String.format("%.1f", roomInfo.roomSize) + "㎡)"
                                binding.detailM2.setImageResource(R.drawable.m2korea)
                                binding.detailSizeUnit.text = sizeUnit
                            }
                        }

                        binding.detailInfoMore.setOnClickListener {
                            val intent = Intent(this@RoomDetailActivity, DetailInfoPopActivity::class.java)
                            intent.putExtra("room_id", roomId)
                            startActivity(intent)
                        }

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


    fun setOptionLayout(option: Option): Int {
        var count = 0
        val setDefaultName :String = "detailOptionword"
        var setName : String
        val bindingClass = binding::class.java

        if(option.bathroom == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "   욕실"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.bed == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "   침실"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.airConditioner == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "  에어컨"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.desk == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "   책상"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.freeParking == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "무료 주차"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.wifi == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "무선 인터넷"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.kitchen == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "주방 공유"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.washer == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "세탁기 공유"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.elevator == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "엘리베이터"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.paidParking == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "유로 주차"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.closet == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "   옷장"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.tv == 1){
            count++
            setName = setDefaultName + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                field.isAccessible = true
                val onView = field.get(binding) as TextView
                onView.text = "   TV"
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        return count
    }

    fun offOptionLayout(count: Int){
        val offDefaultName :String = "optionDetail"
        var offName : String
        val bindingClass = binding::class.java

        for(i in (count+1)..12) {
            //Log.d("i", i.toString())
            offName = offDefaultName + i.toString()
            try {
                val field = bindingClass.getDeclaredField(offName)
                field.isAccessible = true
                val offView =
                    field.get(binding) as View // Change 'View' to the actual type of 'binding.optionDetail12'
                offView.isGone = true
            } catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }
    }

}