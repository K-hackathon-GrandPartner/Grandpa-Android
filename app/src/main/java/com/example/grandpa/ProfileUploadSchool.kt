package com.example.grandpa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileUploadSchool:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_upload_school)

        val backbtn = findViewById<ImageButton>(R.id.profile_backBtn)

        backbtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val regionData = arrayOf("구", "광진구", "노원구", "성북구")
        val Gwangjin_schoolData = arrayOf("학교를 선택하세요","건국대학교", "세종대학교", "장로회신대학교")
        val Nowon_schoolData = arrayOf("학교를 선택하세요","광운대학교", "서울과학기술대학교", "서울여자대학교", "인덕대학교", "한국성서대학교", "삼육대학교")
        val Seongbuk_schoolData = arrayOf("학교를 선택하세요","고려대학교", "한성대학교", "서경대학교", "한국예술종합학교", "동덕여자대학교", "성신여자대학교", "국민대학교")
        val regionSpinner = findViewById<Spinner>(R.id.spinner_location)
        val schoolSpinner = findViewById<Spinner>(R.id.spinner_school)

        val regionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, regionData)
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionSpinner.adapter = regionAdapter

        // 지역 스피너 선택 이벤트 처리
        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (regionData[position]) {
                    "구" -> setSchoolSpinnerValues(schoolSpinner, arrayOf("구를 선택하세요"))
                    "광진구" -> setSchoolSpinnerValues(schoolSpinner, Gwangjin_schoolData)
                    "노원구" -> setSchoolSpinnerValues(schoolSpinner, Nowon_schoolData)
                    "성북구" -> setSchoolSpinnerValues(schoolSpinner, Seongbuk_schoolData)
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"구, 학교를 선택하세요", Toast.LENGTH_SHORT).show()
            }
        }

        //학교 스피너 이벤트처리
        schoolSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val context: Context = applicationContext
                val text = "구, 학교를 선택하세요"
                val duration = Toast.LENGTH_SHORT
                Toast.makeText(context, text, duration).show()
                //+선택안하면 다음 버튼 눌려도 못넘어가도록
            }
        }
    }

    //학교 스피너 값을 설정하는 함수
    private fun setSchoolSpinnerValues(spinner: Spinner, values: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}