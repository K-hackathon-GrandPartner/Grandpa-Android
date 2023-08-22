package com.example.grandpa
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
    @SuppressLint("MissingInflatedId")
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

        //초기화 버튼
        val structureCheckBox = findViewById<LinearLayout>(R.id.linear_structure)
        val optionCheckBox = findViewById<LinearLayout>(R.id.linear_option)
        val reset_btn = findViewById<Button>(R.id.filter_resetBtn)
        reset_btn.setOnClickListener{
            uncheckAllCheckboxes(structureCheckBox)
            uncheckAllCheckboxes(optionCheckBox)
        }

        //보증금 값 범위 가져오기
        val deposit_slider = findViewById<RangeSlider>(R.id.depositrange)
        //SharedPreferences 초기화
        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        deposit_slider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            editor.putFloat("depositFrom", startValue)
            editor.putFloat("depositTo", endValue)
            editor.apply()
        }

        //월세 값 범위 가져오기
        val price_slider = findViewById<RangeSlider>(R.id.pricerange)

        price_slider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            editor.putFloat("monthPriceFrom", startValue)
            editor.putFloat("monthPriceTo", endValue)
            editor.apply()
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

    fun region_onClicked(view: View) {

        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_Gwangjin -> {
                    if(checked) {
                        editor.putBoolean("Gwangjin",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("Gwangjin",false)
                        editor.apply()
                    }
                }
                R.id.filter_Nowon -> {
                    if(checked) {
                        editor.putBoolean("Nowon",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("Nowon",true)
                        editor.apply()
                    }
                }
                R.id.filter_Seongbuk -> {
                    if(checked) {
                        editor.putBoolean("Seongbuk",true)
                        editor.apply()
                    } else {
                        editor.putBoolean("Seongbuk",true)
                        editor.apply()
                    }
                }
            }
        }
    }

    fun buildingType_onClicked(view:View) {

        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_apartment -> {
                    if (checked) {
                        editor.putBoolean("apartment",true)
                    } else {
                        editor.putBoolean("apartment",false)
                    }
                }
                R.id.filter_officetel -> {
                    if (checked) {
                        editor.putBoolean("officetel",true)
                    } else {
                        editor.putBoolean("officetel",false)
                    }
                }
                R.id.filter_villa-> {
                    if (checked) {
                        editor.putBoolean("villa",true)
                    } else {
                        editor.putBoolean("villa",false)
                    }
                }
                R.id.filter_house -> {
                    if (checked) {
                        editor.putBoolean("house",true)
                    } else {
                        editor.putBoolean("house",false)
                    }
                }
            }
        }
    }

    fun roomSize_onClicked(view: View) {

        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_small -> {
                    if(checked) {
                        editor.putBoolean("small",true)
                    } else {
                        editor.putBoolean("small",false)
                    }
                }
                R.id.filter_medium -> {
                    if(checked) {
                        editor.putBoolean("medium",true)
                    } else {
                        editor.putBoolean("medium",false)
                    }
                }
                R.id.filter_big -> {
                    if(checked) {
                        editor.putBoolean("big",true)
                    } else {
                        editor.putBoolean("big",false)
                    }
                }
                R.id.filter_bigger -> {
                    if(checked) {
                        editor.putBoolean("bigger",true)
                    } else {
                        editor.putBoolean("bigger",false)
                    }
                }
            }
        }
    }

    fun option_onClicked(view:View) {

        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when(view.id) {
                R.id.filter_bathroom -> {
                    if(checked) {
                        editor.putBoolean("bathroom",true)
                    } else {
                        editor.putBoolean("bathroom",false)
                    }
                }
                R.id.filter_kitchen -> {
                    if(checked) {
                        editor.putBoolean("kitchen",true)
                    } else {
                        editor.putBoolean("kitchen",false)
                    }
                }
                R.id.filter_bed -> {
                    if(checked) {
                        editor.putBoolean("bed",true)
                    } else {
                        editor.putBoolean("bed",false)
                    }
                }
                R.id.filter_laundary -> {
                    if(checked) {
                        editor.putBoolean("laundary",true)
                    } else {
                        editor.putBoolean("laundary",false)
                    }
                }
                R.id.filter_aircon -> {
                    if(checked) {
                        editor.putBoolean("aircon",true)
                    } else {
                        editor.putBoolean("aircon",false)
                    }
                }
                R.id.filter_elivator -> {
                    if(checked) {
                        editor.putBoolean("elivator",true)
                    } else {
                        editor.putBoolean("elivator",false)
                    }
                }
                R.id.filter_desk -> {
                    if(checked) {
                        editor.putBoolean("desk",true)
                    } else {
                        editor.putBoolean("desk",false)
                    }
                }
                R.id.filter_feeParking -> {
                    if(checked) {
                        editor.putBoolean("feeParking",true)
                    } else {
                        editor.putBoolean("feeParking",false)
                    }
                }
                R.id.filter_freeParking -> {
                    if(checked) {
                        editor.putBoolean("freeParking",true)
                    } else {
                        editor.putBoolean("freeParking",false)
                    }
                }
                R.id.filter_closet -> {
                    if(checked) {
                        editor.putBoolean("closet",true)
                    } else {
                        editor.putBoolean("closet",false)
                    }
                }
                R.id.filter_internet -> {
                    if(checked) {
                        editor.putBoolean("internet",true)
                    } else {
                        editor.putBoolean("internet",false)
                    }
                }
                R.id.filter_tv -> {
                    if(checked) {
                        editor.putBoolean("tv",true)
                    } else {
                        editor.putBoolean("tv",false)
                    }
                }
            }
        }
    }
}