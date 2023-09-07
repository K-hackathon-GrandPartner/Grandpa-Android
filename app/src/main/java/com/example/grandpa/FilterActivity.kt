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

    val filterData = filter_data() // FilterData 클래스의 인스턴스를 생성
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

            filterData.startDeposit = startValue
            filterData.endDeposit = endValue

//            editor.putFloat("startDeposit", startValue)
//            editor.putFloat("endDeposit", endValue)
//            editor.apply()
        }

        //월세 값 범위 내부저장소에 저장
        price_slider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            filterData.startMonthlyRent = startValue
            filterData.endMonthlyRent = endValue

//            editor.putFloat("startMonthlyRent", startValue)
//            editor.putFloat("endMonthlyRent", endValue)
//            editor.apply()
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
            putData()//db에 저장
            //방 조회로 화면 전환
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun putData() {
        //SharedPreferences 초기화
        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()
        //filter_data 내의 속성들 변수에 대입
        val Properties = filterData.javaClass.declaredFields

        for(property in Properties){
            property.isAccessible = true //속성 접근 권한 변경
            val propertyName = property.name
            val propertyValue = property.get(filterData)
            if (propertyValue != null){
                if(propertyName.contains("start") || propertyName.contains("end")){
                    editor.putFloat(propertyName, propertyValue as Float)
                    editor.apply()
                } else{
                    editor.putString(propertyName, propertyValue.toString())
                    editor.apply()
                }
            }
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
                        filterData.region_Gwangjin = "광진구"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("region1","광진구")
//                        editor.apply()
                    } else {
                        filterData.region_Gwangjin = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("region1")
//                        editor.apply()
                    }
                }
                R.id.filter_Nowon -> {
                    if(checked) {
                        filterData.region_Nowon = "노원구"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("region2","노원구")
//                        editor.apply()
                    } else {
                        filterData.region_Nowon = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("region2")
//                        editor.apply()
                    }
                }
                R.id.filter_Seongbuk -> {
                    if(checked) {
                        filterData.region_Seongbuk = "성북구"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("region3","성북구")
//                        editor.apply()
                    } else {
                        filterData.region_Seongbuk = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("region3")
//                        editor.apply()
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
                        filterData.buildingType_apartment = "아파트"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("buildingType1","아파트")
//                        editor.apply()
                    } else {
                        filterData.buildingType_apartment = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("buildingType1")
//                        editor.apply()
                    }
                }
                R.id.filter_officetel -> {
                    if (checked) {
                        filterData.buildingType_officetel = "오피스텔"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("buildingType2","오피스텔")
//                        editor.apply()
                    } else {
                        filterData.buildingType_officetel = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("buildingType2")
//                        editor.apply()
                    }
                }
                R.id.filter_villa-> {
                    if (checked) {
                        filterData.buildingType_villa = "빌라"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("buildingType3","빌라")
//                        editor.apply()
                    } else {
                        filterData.buildingType_villa = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("buildingType3")
//                        editor.apply()
                    }
                }
                R.id.filter_house -> {
                    if (checked) {
                        filterData.buildingType_house = "단독 주택"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("buildingType4","단독 주택")
//                        editor.apply()
                    } else {
                        filterData.buildingType_house = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("buildingType4")
//                        editor.apply()
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
                        filterData.roomSize_small = "소형"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("roomSize1","소형")
//                        editor.apply()
                    } else {
                        filterData.roomSize_small = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("roomSize1")
//                        editor.apply()
                    }
                }
                R.id.filter_medium -> {
                    if(checked) {
                        filterData.roomSize_medium = "중형"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("roomSize2","중형")
//                        editor.apply()
                    } else {
                        filterData.roomSize_medium = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("roomSize2")
//                        editor.apply()
                    }
                }
                R.id.filter_big -> {
                    if(checked) {
                        filterData.roomSize_big = "대형"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("roomSize3","대형")
//                        editor.apply()
                    } else {
                        filterData.roomSize_big = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("roomSize3")
//                        editor.apply()
                    }
                }
                R.id.filter_bigger -> {
                    if(checked) {
                        filterData.roomSize_bigger = "대형+"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("roomSize4","대형+")
//                        editor.apply()
                    } else {
                        filterData.roomSize_bigger = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("roomSize4")
//                        editor.apply()
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
                        filterData.option_bathroom = "욕실"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option1","욕실")
//                        editor.apply()
                    } else {
                        filterData.option_bathroom = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option1")
//                        editor.apply()
                    }
                }
                R.id.filter_kitchen -> {
                    if(checked) {
                        filterData.option_kitchen = "주방 공유"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option2","주방 공유")
//                        editor.apply()
                    } else {
                        filterData.option_kitchen = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option2")
//                        editor.apply()
                    }
                }
                R.id.filter_bed -> {
                    if(checked) {
                        filterData.option_bed = "침대"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option3","침대")
//                        editor.apply()
                    } else {
                        filterData.option_bed = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option3")
//                        editor.apply()
                    }
                }
                R.id.filter_laundry -> {
                    if(checked) {
                        filterData.option_laundry = "세탁기 공유"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option4","세탁기 공유")
//                        editor.apply()
                    } else {
                        filterData.option_laundry = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option4")
//                        editor.apply()
                    }
                }
                R.id.filter_aircon -> {
                    if(checked) {
                        filterData.option_aircon = "에어컨"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option5","에어컨")
//                        editor.apply()
                    } else {
                        filterData.option_aircon = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option5")
//                        editor.apply()
                    }
                }
                R.id.filter_elevator -> {
                    if(checked) {
                        filterData.option_elevator = "엘리베이터"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option6","엘리베이터")
//                        editor.apply()
                    } else {
                        filterData.option_elevator = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option6")
//                        editor.apply()
                    }
                }
                R.id.filter_desk -> {
                    if(checked) {
                        filterData.option_desk = "책상"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option7","책상")
//                        editor.apply()
                    } else {
                        filterData.option_desk = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option7")
//                        editor.apply()
                    }
                }
                R.id.filter_feeParking -> {
                    if(checked) {
                        filterData.option_feeParking = "유료 주차"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option8","유료 주차")
//                        editor.apply()
                    } else {
                        filterData.option_feeParking = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option8")
//                        editor.apply()
                    }
                }
                R.id.filter_freeParking -> {
                    if(checked) {
                        filterData.option_freeParking = "무료 주차"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option9","무료 주차")
//                        editor.apply()
                    } else {
                        filterData.option_freeParking = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option9")
//                        editor.apply()
                    }
                }
                R.id.filter_closet -> {
                    if(checked) {
                        filterData.option_closet = "옷장"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option10","옷장")
//                        editor.apply()
                    } else {
                        filterData.option_closet = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option10")
//                        editor.apply()
                    }
                }
                R.id.filter_internet -> {
                    if(checked) {
                        filterData.option_internet = "무선 인터넷"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option11","무선 인터넷")
//                        editor.apply()
                    } else {
                        filterData.option_internet = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option11")
//                        editor.apply()
                    }
                }
                R.id.filter_tv -> {
                    if(checked) {
                        filterData.option_tv = "TV"
                        Log.d(TAG, filterData.toString())
//                        editor.putString("option12","TV")
//                        editor.apply()
                    } else {
                        filterData.option_tv = null
                        Log.d(TAG, filterData.toString())
//                        editor.remove("option12")
//                        editor.apply()
                    }
                }
            }
        }
    }
}