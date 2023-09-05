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
import com.google.android.material.slider.RangeSlider


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

            editor.putFloat("depositFrom", startValue)
            editor.putFloat("depositTo", endValue)
            editor.apply()
        }

        //월세 값 범위 내부저장소에 저장
        price_slider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            editor.putFloat("monthPriceFrom", startValue)
            editor.putFloat("monthPriceTo", endValue)
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
                        editor.putBoolean("Gwangjin",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("Gwangjin",false)
                        editor.remove("Gwangjin")
                        editor.apply()
                    }
                }
                R.id.filter_Nowon -> {
                    if(checked) {
                        editor.putBoolean("Nowon",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("Nowon",true)
                        editor.remove("Nowon")
                        editor.apply()
                    }
                }
                R.id.filter_Seongbuk -> {
                    if(checked) {
                        editor.putBoolean("Seongbuk",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("Seongbuk",true)
                        editor.remove("Seongbuk")
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
                        editor.putBoolean("apartment",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("apartment",false)
                        editor.remove("apartment")
                        editor.apply()
                    }
                }
                R.id.filter_officetel -> {
                    if (checked) {
                        editor.putBoolean("officetel",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("officetel",false)
                        editor.remove("officetel")
                        editor.apply()
                    }
                }
                R.id.filter_villa-> {
                    if (checked) {
                        editor.putBoolean("villa",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("villa",false)
                        editor.remove("villa")
                        editor.apply()
                    }
                }
                R.id.filter_house -> {
                    if (checked) {
                        editor.putBoolean("house",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("house",false)
                        editor.remove("house")
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
                        editor.putBoolean("small",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("small",false)
                        editor.remove("small")
                        editor.apply()
                    }
                }
                R.id.filter_medium -> {
                    if(checked) {
                        editor.putBoolean("medium",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("medium",false)
                        editor.remove("medium")
                        editor.apply()
                    }
                }
                R.id.filter_big -> {
                    if(checked) {
                        editor.putBoolean("big",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("big",false)
                        editor.remove("big")
                        editor.apply()
                    }
                }
                R.id.filter_bigger -> {
                    if(checked) {
                        editor.putBoolean("bigger",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("bigger",false)
                        editor.remove("bigger")
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
                        editor.putBoolean("bathroom",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("bathroom",false)
                        editor.remove("bathroom")
                        editor.apply()
                    }
                }
                R.id.filter_kitchen -> {
                    if(checked) {
                        editor.putBoolean("kitchen",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("kitchen",false)
                        editor.remove("kitchen")
                        editor.apply()
                    }
                }
                R.id.filter_bed -> {
                    if(checked) {
                        editor.putBoolean("bed",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("bed",false)
                        editor.remove("bed")
                        editor.apply()
                    }
                }
                R.id.filter_laundry -> {
                    if(checked) {
                        editor.putBoolean("laundry",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("laundry",false)
                        editor.remove("laundry")
                        editor.apply()
                    }
                }
                R.id.filter_aircon -> {
                    if(checked) {
                        editor.putBoolean("aircon",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("aircon",false)
                        editor.remove("aircon")
                        editor.apply()
                    }
                }
                R.id.filter_elevator -> {
                    if(checked) {
                        editor.putBoolean("elevator",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("elevator",false)
                        editor.remove("elevator")
                        editor.apply()
                    }
                }
                R.id.filter_desk -> {
                    if(checked) {
                        editor.putBoolean("desk",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("desk",false)
                        editor.remove("desk")
                        editor.apply()
                    }
                }
                R.id.filter_feeParking -> {
                    if(checked) {
                        editor.putBoolean("feeParking",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("feeParking",false)
                        editor.remove("feeParking")
                        editor.apply()
                    }
                }
                R.id.filter_freeParking -> {
                    if(checked) {
                        editor.putBoolean("freeParking",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("freeParking",false)
                        editor.remove("freeParking")
                        editor.apply()
                    }
                }
                R.id.filter_closet -> {
                    if(checked) {
                        editor.putBoolean("closet",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("closet",false)
                        editor.remove("closet")
                        editor.apply()
                    }
                }
                R.id.filter_internet -> {
                    if(checked) {
                        editor.putBoolean("internet",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("internet",false)
                        editor.remove("internet")
                        editor.apply()
                    }
                }
                R.id.filter_tv -> {
                    if(checked) {
                        editor.putBoolean("tv",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("tv",false)
                        editor.remove("tv")
                        editor.apply()
                    }
                }
            }
        }
    }
}