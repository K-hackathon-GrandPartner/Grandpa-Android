package com.example.grandpa
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider

class FilterActivity : AppCompatActivity() {
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

        //보증금,월세 값 범위 가져오기
        val deposit_slider = findViewById<RangeSlider>(R.id.depositrange)

        deposit_slider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            filter_data(depositFrom = startValue)
            filter_data(depositTo = endValue)

            Log.d("보증금", ": $startValue ~ $endValue")
            println("보증금 데이터 클래스 값: ${filteringData.depositFrom} ~ ${filteringData.depositTo}")
        }

        val price_slider = findViewById<RangeSlider>(R.id.pricerange)

        price_slider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            filter_data(monthPriceFrom = startValue)
            filter_data(monthPriceTo = endValue)

            Log.d("월세", ": $startValue ~ $endValue")
            println("월세 데이터 클래스 값: ${filteringData.monthPriceFrom} ~ ${filteringData.monthPriceTo}")
        }
    }

    val filteringData = filter_data()

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
        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_Gwangjin -> {
                    if(checked) {
                        //광진구 필터 적용되도록
                        //filter_data(Gwangjin = true)
                        filteringData.copy(Gwangjin = checked)
                        println(filteringData.Gwangjin)
                    } else {
                        //광진구 필터 해제되도록
                        filter_data(Gwangjin = false)
                        println(filteringData.Gwangjin)
                    }
                }
                R.id.filter_Nowon -> {
                    if(checked) {
                        filter_data(Nowon = true)
                    } else {
                        filter_data(Nowon = false)
                    }
                }
                R.id.filter_Seongbuk -> {
                    if(checked) {
                        filter_data(Seongbuk = true)
                    } else {
                        filter_data(Seongbuk = false)
                    }
                }
            }
        }
    }

    fun buildingType_onClicked(view:View) {
        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_apartment -> {
                    if (checked) {
                        filter_data(apartment = true)
                    } else {
                        filter_data(apartment = false)
                    }
                }
                R.id.filter_officetel -> {
                    if (checked) {
                        filter_data(officetel = true)
                    } else {
                        filter_data(officetel = false)
                    }
                }
                R.id.filter_villa-> {
                    if (checked) {
                        filter_data(villa = true)
                    } else {
                        filter_data(villa = false)
                    }
                }
                R.id.filter_house -> {
                    if (checked) {
                        filter_data(house = true)
                    } else {
                        filter_data(house = false)
                    }
                }
            }
        }
    }

    fun roomSize_onClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_small -> {
                    if(checked) {
                        filter_data(small = true)
                    } else {
                        filter_data(small = false)
                    }
                }
                R.id.filter_medium -> {
                    if(checked) {
                        filter_data(medium = true)
                    } else {
                        filter_data(medium = false)
                    }
                }
                R.id.filter_big -> {
                    if(checked) {
                        filter_data(big = true)
                    } else {
                        filter_data(big = false)
                    }
                }
                R.id.filter_bigger -> {
                    if(checked) {
                        filter_data(bigger = true)
                    } else {
                        filter_data(bigger = false)
                    }
                }
            }
        }
    }

    fun option_onClicked(view:View) {
        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when(view.id) {
                R.id.filter_bathroom -> {
                    if(checked) {
                        filter_data(bathroom = true)
                    } else {
                        filter_data(bathroom = false)
                    }
                }
                R.id.filter_kitchen -> {
                    if(checked) {
                        filter_data(kitchen = true)
                    } else {
                        filter_data(kitchen = false)
                    }
                }
                R.id.filter_bed -> {
                    if(checked) {
                        filter_data(bed = true)
                    } else {
                        filter_data(bed = false)
                    }
                }
                R.id.filter_laundary -> {
                    if(checked) {
                        filter_data(laundary = true)
                    } else {
                        filter_data(laundary = false)
                    }
                }
                R.id.filter_aircon -> {
                    if(checked) {
                        filter_data(aircon = true)
                    } else {
                        filter_data(aircon = false)
                    }
                }
                R.id.filter_elivator -> {
                    if(checked) {
                        filter_data(elivator = true)
                    } else {
                        filter_data(elivator = false)
                    }
                }
                R.id.filter_desk -> {
                    if(checked) {
                        filter_data(desk = true)
                    } else {
                        filter_data(desk = false)
                    }
                }
                R.id.filter_feeParking -> {
                    if(checked) {
                        filter_data(feeParking = true)
                    } else {
                        filter_data(feeParking = false)
                    }
                }
                R.id.filter_freeParking -> {
                    if(checked) {
                        filter_data(freeParking = true)
                    } else {
                        filter_data(freeParking = false)
                    }
                }
                R.id.filter_closet -> {
                    if(checked) {
                        filter_data(closet = true)
                    } else {
                        filter_data(closet = false)
                    }
                }
                R.id.filter_internet -> {
                    if(checked) {
                        filter_data(internet = true)
                    } else {
                        filter_data(internet = false)
                    }
                }
                R.id.filter_tv -> {
                    if(checked) {
                        filter_data(tv = true)
                        println(filteringData.tv)
                    } else {
                        filter_data(tv = false)
                        println(filteringData.tv)
                    }
                }
            }
        }
    }
}