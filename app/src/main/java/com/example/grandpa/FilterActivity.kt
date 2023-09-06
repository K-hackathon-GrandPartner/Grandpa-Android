package com.example.grandpa
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.grandpa.FilteredRoomImpl.apiService
import com.google.android.material.slider.RangeSlider
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilterActivity : AppCompatActivity() {

    object FilteringDB {
        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences("FilterData", Context.MODE_PRIVATE)
        }

        fun getInstance(): SharedPreferences {
            if(!this::sharedPreferences.isInitialized) {
                throw java.lang.IllegalStateException("SharedPreferencesSingleton is not initialized")
            }
            return sharedPreferences
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filtering_room) // filter_roomInfo.xml 파일과 연결

        //roomlist 개수 가져옴
        val roomListSize = intent.getIntExtra("roomListSize", 0) //0은 기본 값
        val sumOfRoom = findViewById<TextView>(R.id.CountRoom)
        sumOfRoom.text = "총 ${roomListSize} 개"

        //Back 버튼
        val backImageView : ImageView = findViewById(R.id.filtering_backbtn)
        backImageView.setOnClickListener{
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish()
        }

        val structureCheckBox = findViewById<LinearLayout>(R.id.linear_structure)
        val optionCheckBox = findViewById<LinearLayout>(R.id.linear_option)
        val reset_btn = findViewById<Button>(R.id.filter_resetBtn)
        val apply_btn = findViewById<Button>(R.id.filter_applyBtn)
        val deposit_slider = findViewById<RangeSlider>(R.id.depositrange)
        val price_slider = findViewById<RangeSlider>(R.id.pricerange)

        //SharedPreferences 초기화
        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        //보증금 값 범위 내부저장소에 저장
        deposit_slider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            editor.putFloat("startDeposit", startValue)
            editor.putFloat("endDeposit", endValue)
            editor.apply()
        }

        //월세 값 범위 내부저장소에 저장
        price_slider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            editor.putFloat("startMonthlyRent", startValue)
            editor.putFloat("endMonthlyRent", endValue)
            editor.apply()
        }

        //초기화 버튼
        reset_btn.setOnClickListener{
            uncheckAllCheckboxes(structureCheckBox)
            uncheckAllCheckboxes(optionCheckBox)

            val depositMin = 0.0f
            val depositMax = 1000.0f
            val priceMin = 0.0f
            val priceMax = 100.0f

            // deposit_slider와 price_slider의 값을 설정
            deposit_slider.values = listOf(depositMin, depositMax)
            price_slider.values = listOf(priceMin, priceMax)

            editor.clear()
            editor.apply()
        }

        //적용하기 버튼
        apply_btn.setOnClickListener{



        }

    }

    @SuppressLint("CommitPrefEdits")
    fun getFilteredRooms() {
        //SharedPreferences 초기화
        FilteringDB.init(this)
        val filterDB = FilteringDB.getInstance()
        val keys = filterDB.all.keys // 모든 SharedPreferences 키를 가져옴
        // 쿼리 파라미터를 Map으로 만들기(보증금,월세)
        val queryParams = mutableMapOf<String,Float>()
        val regionParams = mutableListOf<String>()
        val buildingTypeParams = mutableListOf<String>()
        val roomSizeParams = mutableListOf<String>()
        val optionParams = mutableListOf<String>()

        for (key in keys) {
            if(key.contains("start") || key.contains("end")){
                val value = filterDB.all[key]
                queryParams[key] = value as Float
            }
            else if(key.contains("region")){
                val value = filterDB.all[key]
                regionParams.add(value.toString())
            }
            else if(key.contains("buildingType")){
                val value = filterDB.all[key]
                buildingTypeParams.add(value.toString())
            }
            else if(key.contains("roomSize")){
                val value = filterDB.all[key]
                roomSizeParams.add(value.toString())
            }
            else if(key.contains("option")){
                val value = filterDB.all[key]
                optionParams.add(value.toString())
            }
        }

        // Retrofit 서비스 인터페이스를 사용하여 API 요청 보내기
        val call = apiService.requestList(queryParams, regionParams, buildingTypeParams, roomSizeParams, optionParams)

        call.enqueue(object : Callback<FilteredRoomResponse> {
            override fun onResponse(call: Call<FilteredRoomResponse>, response: Response<FilteredRoomResponse>) {
                if (response.isSuccessful) {
                    // 응답 성공
                    val data = response.body()
                    Log.d("API1", data.toString())
                } else {
                    // 응답 실패
                }
            }

            override fun onFailure(call: Call<FilteredRoomResponse>, t: Throwable) {
                // 네트워크 오류 또는 예외 처리
                Log.d("API1", "fail")
            }
        })


    }




    //체크 박스 찾아서 해제 하는 함수
    private fun uncheckAllCheckboxes(parentView: View) {
        if (parentView is ViewGroup) {
            for (i in 0 until parentView.childCount) {
                val childView = parentView.getChildAt(i)
                if (childView is CheckBox) {
                    childView.isChecked = false
                } else if (childView is ViewGroup) {
                    uncheckAllCheckboxes(childView)
                }
            }
        }
    }

    fun regionOnClicked(view: View) {

        //SharedPreferences 초기화
        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit() //내부저장소 접근객체 생성

        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_Gwangjin -> {
                    if(checked) {
                        editor.putString("region1","광진구")
                        editor.apply()
                    } else {
                        editor.remove("region1")
                        editor.apply()
                    }
                }
                R.id.filter_Nowon -> {
                    if(checked) {
                        editor.putString("region2","노원구")
                        editor.apply()
                    } else {
                        editor.remove("region2")
                        editor.apply()
                    }
                }
                R.id.filter_Seongbuk -> {
                    if(checked) {
                        editor.putString("region3","성북구")
                        editor.apply()
                    } else {
                        editor.remove("region3")
                        editor.apply()
                    }
                }
            }
        }
    }

    fun buildingTypeOnClicked(view:View) {

        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_apartment -> {
                    if (checked) {
                        editor.putString("buildingType1","아파트")
                        editor.apply()
                    } else {
                        editor.remove("buildingType1")
                        editor.apply()
                    }
                }
                R.id.filter_officetel -> {
                    if (checked) {
                        editor.putString("buildingType2","오피스텔")
                        editor.apply()
                    } else {
                        editor.remove("buildingType2")
                        editor.apply()
                    }
                }
                R.id.filter_villa-> {
                    if (checked) {
                        editor.putString("buildingType3","빌라")
                        editor.apply()
                    } else {
                        editor.remove("buildingType3")
                        editor.apply()
                    }
                }
                R.id.filter_house -> {
                    if (checked) {
                        editor.putString("buildingType4","단독 주택")
                        editor.apply()
                    } else {
                        editor.remove("buildingType4")
                        editor.apply()
                    }
                }
            }
        }
    }

    fun roomSizeOnClicked(view: View) {

        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_small -> {
                    if(checked) {
                        editor.putString("roomSize1","소형")
                        editor.apply()
                    } else {
                        editor.remove("roomSize1")
                        editor.apply()
                    }
                }
                R.id.filter_medium -> {
                    if(checked) {
                        editor.putString("roomSize2","중형")
                        editor.apply()
                    } else {
                        editor.remove("roomSize2")
                        editor.apply()
                    }
                }
                R.id.filter_big -> {
                    if(checked) {
                        editor.putString("roomSize3","대형")
                        editor.apply()
                    } else {
                        editor.remove("roomSize3")
                        editor.apply()
                    }
                }
                R.id.filter_bigger -> {
                    if(checked) {
                        editor.putString("roomSize4","대형+")
                        editor.apply()
                    } else {
                        editor.remove("roomSize4")
                        editor.apply()
                    }
                }
            }
        }
    }

    fun optionOnClicked(view:View) {

        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when(view.id) {
                R.id.filter_bathroom -> {
                    if(checked) {
                        editor.putString("option1","욕실")
                        editor.apply()
                    } else {
                        editor.remove("option1")
                        editor.apply()
                    }
                }
                R.id.filter_kitchen -> {
                    if(checked) {
                        editor.putString("option2","주방 공유")
                        editor.apply()
                    } else {
                        editor.remove("option2")
                        editor.apply()
                    }
                }
                R.id.filter_bed -> {
                    if(checked) {
                        editor.putString("option3","침대")
                        editor.apply()
                    } else {
                        editor.remove("option3")
                        editor.apply()
                    }
                }
                R.id.filter_laundry -> {
                    if(checked) {
                        editor.putString("option4","세탁기 공유")
                        editor.apply()
                    } else {
                        editor.remove("option4")
                        editor.apply()
                    }
                }
                R.id.filter_aircon -> {
                    if(checked) {
                        editor.putString("option5","에어컨")
                        editor.apply()
                    } else {
                        editor.remove("option5")
                        editor.apply()
                    }
                }
                R.id.filter_elevator -> {
                    if(checked) {
                        editor.putString("option6","엘리베이터")
                        editor.apply()
                    } else {
                        editor.remove("option6")
                        editor.apply()
                    }
                }
                R.id.filter_desk -> {
                    if(checked) {
                        editor.putString("option7","책상")
                        editor.apply()
                    } else {
                        editor.remove("option7")
                        editor.apply()
                    }
                }
                R.id.filter_feeParking -> {
                    if(checked) {
                        editor.putString("option8","유료 주차")
                        editor.apply()
                    } else {
                        editor.remove("option8")
                        editor.apply()
                    }
                }
                R.id.filter_freeParking -> {
                    if(checked) {
                        editor.putString("option9","무료 주차")
                        editor.apply()
                    } else {
                        editor.remove("option9")
                        editor.apply()
                    }
                }
                R.id.filter_closet -> {
                    if(checked) {
                        editor.putString("option10","옷장")
                        editor.apply()
                    } else {
                        editor.remove("option10")
                        editor.apply()
                    }
                }
                R.id.filter_internet -> {
                    if(checked) {
                        editor.putString("option11","무선 인터넷")
                        editor.apply()
                    } else {
                        editor.remove("option11")
                        editor.apply()
                    }
                }
                R.id.filter_tv -> {
                    if(checked) {
                        editor.putString("option12","TV")
                        editor.apply()
                    } else {
                        editor.remove("option12")
                        editor.apply()
                    }
                }
            }
        }
    }
}