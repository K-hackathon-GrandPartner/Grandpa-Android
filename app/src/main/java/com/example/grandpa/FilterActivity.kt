package com.example.grandpa
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
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

    private val filterData = filter_data() // FilterData 클래스의 인스턴스를 생성
    object FilteringDB {
        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences("FilterData", Context.MODE_PRIVATE)
        }

        fun getInstance(): SharedPreferences {
            if(!this::sharedPreferences.isInitialized) {
                throw IllegalStateException("SharedPreferencesSingleton is not initialized")
            }
            return sharedPreferences
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filtering_room) // filter_roomInfo.xml 파일과 연결

        fixFiltering()
        fixRangeSlider()

        //roomlist 개수 가져옴
        val roomListSize = intent.getIntExtra("roomListSize", 0) //0은 기본 값
        val sumOfRoom = findViewById<TextView>(R.id.CountRoom)
        sumOfRoom.text =  "$roomListSize"

        //Back 버튼
        val backImageView: ImageView = findViewById(R.id.filtering_backbtn)
        backImageView.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
            finish()
        }

        val structureCheckBox = findViewById<LinearLayout>(R.id.linear_structure)
        val optionCheckBox = findViewById<LinearLayout>(R.id.linear_option)
        val depositSlider = findViewById<RangeSlider>(R.id.depositrange)
        val priceSlider = findViewById<RangeSlider>(R.id.pricerange)
        val reset_btn = findViewById<Button>(R.id.filter_resetBtn)
        val apply_btn = findViewById<Button>(R.id.filter_applyBtn)

        fixFiltering() //값 고정
        fixRangeSlider() //슬라이더 값 고정

        //SharedPreferences 초기화
        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()

        //보증금 값 범위 데이터셋에 저장
        depositSlider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            filterData.startDeposit = startValue
            filterData.endDeposit = endValue
        }

        //월세 값 범위 데이터셋에 저장
        priceSlider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val startValue = values[0]
            val endValue = values[1]

            filterData.startMonthlyRent = startValue
            filterData.endMonthlyRent = endValue
        }

        //초기화 버튼
        reset_btn.setOnClickListener {
            uncheckAllCheckboxes(structureCheckBox)
            uncheckAllCheckboxes(optionCheckBox)

            var depositMin = 0.0f
            var depositMax = 1000.0f
            var monthlyRentMin = 0.0f
            var monthlyRentMax = 100.0f

            // deposit_slider와 price_slider의 값을 설정
            depositSlider.values = listOf(depositMin, depositMax)
            priceSlider.values = listOf(monthlyRentMin, monthlyRentMax)

            editor.clear()
            editor.apply()
        }


        //적용하기 버튼
        apply_btn.setOnClickListener {
            putData()//db에 저장

            //방 조회로 화면 전환
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
            finish()
        }

        val searchImageView: ImageView = findViewById(R.id.filtering_search)
        searchImageView.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val heartImageView: ImageView = findViewById(R.id.filtering_heart)
        heartImageView.setOnClickListener {
            val intent = Intent(this, HeartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val chatImageView: ImageView = findViewById(R.id.filtering_magazine)
        chatImageView.setOnClickListener{
            val intent = Intent(this, MagazineActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val checkImageView: ImageView = findViewById(R.id.filtering_check)
        checkImageView.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }

        val profileImageView: ImageView = findViewById(R.id.filtering_profile)
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
            finish()
        }
    }


    //데이터셋의 값 db에 저장하는 함수
    private fun putData() {
        //SharedPreferences 초기화
        FilteringDB.init(this)
        val editor = FilteringDB.getInstance().edit()
        //filter_data 내의 속성들 변수에 대입
        val properties = filterData.javaClass.declaredFields

        for(property in properties){
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

    //체크 박스 찾아서 체크하는 함수
    private fun checkCheckboxes(parentView: View, data: String) {
        if(parentView is ViewGroup) {
            for(i in 0 until parentView.childCount) {
                val childView = parentView.getChildAt(i)
                if(childView is CheckBox) {
                    val childText = childView.text.toString()
                    if(childText == data){
                        childView.isChecked = true
                    }
                } else if (childView is ViewGroup) {
                    checkCheckboxes(childView, data)
                }
            }
        }
    }

    //rangeSlider 고정 함수
    private fun fixRangeSlider() {
        FilteringDB.init(this)
        var db = FilteringDB.getInstance()
        val depositSlider = findViewById<RangeSlider>(R.id.depositrange)
        val priceSlider = findViewById<RangeSlider>(R.id.pricerange)

        val depositMin = db.getFloat("startDeposit", 0.0f)
        val depositMax = db.getFloat("endDeposit", 1000.0f)
        val monthlyRentMin = db.getFloat("startMonthlyRent", 0.0f)
        val monthlyRentMax = db.getFloat("endMonthlyRen", 100.0f)

        // RangeSlider에 값 설정
        depositSlider.values = listOf(depositMin, depositMax)
        priceSlider.values = listOf(monthlyRentMin, monthlyRentMax)
    }


    //필터링 고정 함수
    private fun fixFiltering() {
        FilteringDB.init(this)
        val db = FilteringDB.getInstance()
        val structureCheckBox = findViewById<LinearLayout>(R.id.linear_structure)
        val optionCheckBox = findViewById<LinearLayout>(R.id.linear_option)
        val allEntries :HashMap<String, *> = db.all as HashMap<String, *>

        //db에 값이 하나라도 있다면
        if(allEntries != null){
            for((key, value) in allEntries){
                if(value is String) {
                    if(key.contains("option")) {
                        checkCheckboxes(optionCheckBox, value)
                    } else {
                        checkCheckboxes(structureCheckBox, value)
                    }
                }
            }
        }
    }

    //체크박스 상태 확인 및 값 변경 함수
    fun regionOnClicked(view: View) {
        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_Gwangjin -> {
                    if(checked) {
                        filterData.region_Gwangjin = "광진구"
                    } else {
                        filterData.region_Gwangjin = null
                    }
                }
                R.id.filter_Nowon -> {
                    if(checked) {
                        filterData.region_Nowon = "노원구"
                    } else {
                        filterData.region_Nowon = null
                    }
                }
                R.id.filter_Seongbuk -> {
                    if(checked) {
                        filterData.region_Seongbuk = "성북구"
                    } else {
                        filterData.region_Seongbuk = null
                    }
                }
            }
        }
    }

    fun buildingTypeOnClicked(view:View) {

        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_apartment -> {
                    if (checked) {
                        filterData.buildingType_apartment = "아파트"
                    } else {
                        filterData.buildingType_apartment = null
                    }
                }
                R.id.filter_officetel -> {
                    if (checked) {
                        filterData.buildingType_officetel = "오피스텔"
                    } else {
                        filterData.buildingType_officetel = null
                    }
                }
                R.id.filter_villa-> {
                    if (checked) {
                        filterData.buildingType_villa = "빌라"
                    } else {
                        filterData.buildingType_villa = null
                    }
                }
                R.id.filter_house -> {
                    if (checked) {
                        filterData.buildingType_house = "단독 주택"
                    } else {
                        filterData.buildingType_house = null
                    }
                }
            }
        }
    }

    fun roomSizeOnClicked(view: View) {

        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.filter_small -> {
                    if(checked) {
                        filterData.roomSize_small = "소형"
                    } else {
                        filterData.roomSize_small = null
                    }
                }
                R.id.filter_medium -> {
                    if(checked) {
                        filterData.roomSize_medium = "중형"
                    } else {
                        filterData.roomSize_medium = null
                    }
                }
                R.id.filter_big -> {
                    if(checked) {
                        filterData.roomSize_big = "대형"
                    } else {
                        filterData.roomSize_big = null
                    }
                }
                R.id.filter_bigger -> {
                    if(checked) {
                        filterData.roomSize_bigger = "대형+"
                    } else {
                        filterData.roomSize_bigger = null
                    }
                }
            }
        }
    }

    fun optionOnClicked(view:View) {

        if(view is CheckBox) {
            val checked: Boolean = view.isChecked

            when(view.id) {
                R.id.filter_bathroom -> {
                    if(checked) {
                        filterData.option_bathroom = "욕실"
                    } else {
                        filterData.option_bathroom = null
                    }
                }
                R.id.filter_kitchen -> {
                    if(checked) {
                        filterData.option_kitchen = "주방 공유"
                    } else {
                        filterData.option_kitchen = null
                    }
                }
                R.id.filter_bed -> {
                    if(checked) {
                        filterData.option_bed = "침대"
                    } else {
                        filterData.option_bed = null
                    }
                }
                R.id.filter_laundry -> {
                    if(checked) {
                        filterData.option_laundry = "세탁기 공유"
                    } else {
                        filterData.option_laundry = null
                    }
                }
                R.id.filter_aircon -> {
                    if(checked) {
                        filterData.option_aircon = "에어컨"
                    } else {
                        filterData.option_aircon = null
                    }
                }
                R.id.filter_elevator -> {
                    if(checked) {
                        filterData.option_elevator = "엘리베이터"
                    } else {
                        filterData.option_elevator = null
                    }
                }
                R.id.filter_desk -> {
                    if(checked) {
                        filterData.option_desk = "책상"
                    } else {
                        filterData.option_desk = null
                    }
                }
                R.id.filter_feeParking -> {
                    if(checked) {
                        filterData.option_feeParking = "유료 주차"
                    } else {
                        filterData.option_feeParking = null
                    }
                }
                R.id.filter_freeParking -> {
                    if(checked) {
                        filterData.option_freeParking = "무료 주차"
                    } else {
                        filterData.option_freeParking = null
                    }
                }
                R.id.filter_closet -> {
                    if(checked) {
                        filterData.option_closet = "옷장"
                    } else {
                        filterData.option_closet = null
                    }
                }
                R.id.filter_internet -> {
                    if(checked) {
                        filterData.option_internet = "무선 인터넷"
                    } else {
                        filterData.option_internet = null
                    }
                }
                R.id.filter_tv -> {
                    if(checked) {
                        filterData.option_tv = "TV"
                    } else {
                        filterData.option_tv = null
                    }
                }
            }
        }
    }
}