package com.example.grandpa

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.viewpager2.widget.ViewPager2
import com.example.grandpa.databinding.RoomDetailBinding
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeartDetailActivity: AppCompatActivity() , OnMapReadyCallback {
    var setM2 : Boolean = false
    private lateinit var roomInfo : room_detail_data

    private val binding by lazy{
        RoomDetailBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.detailHeart.setImageResource(R.drawable.onheart)

        binding.detailBack.setOnClickListener{
            finish()
        }

        binding.detailSearch.setOnClickListener {
            val intent = Intent(this, ShowRoomActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.detailCheck.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.detailMagazine.setOnClickListener {
            val intent = Intent(this, MagazineActivity::class.java)
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


        // 찜 상태에 따라 이미지 변환
        RoomDetailActivity.HeartDB.init(this)
        val InfoData = RoomDetailActivity.HeartDB.getInstance()
        if (InfoData.contains(roomId.toString())){
            binding.detailStorageheart.setImageResource(R.drawable.onblackheart)
        }else{
            binding.detailStorageheart.setImageResource(R.drawable.offblackheart)
        }

        val value = InfoData.getString(roomId.toString(), null)
        val gson = Gson()
        roomInfo = gson.fromJson(value, room_detail_data::class.java)

        //이미지 가로 전환
        val roomImagePager: ViewPager2 = findViewById(R.id.viewPager2)
        roomImagePager.adapter = RoomImageAdapter(roomInfo.images, this)
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
        Log.d("count", count.toString())
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

        // 하트 눌렀을 때
        binding.detailStorageheart.setOnClickListener {
            RoomDetailActivity.HeartDB.init(this)
            val InfoData = RoomDetailActivity.HeartDB.getInstance()

            if(InfoData.contains(roomInfo.id.toString())){
                val editor = InfoData.edit()
                editor.remove(roomInfo.id.toString())
                editor.apply()
                binding.detailStorageheart.setImageResource(R.drawable.offblackheart)
            }else{
                val editor = InfoData.edit()
                val key = roomInfo.id.toString()
                val gson = Gson()
                val roomInfoJson = gson.toJson(roomInfo)
                Log.d("roomInfoJso", roomInfoJson)
                editor.putString(key, roomInfoJson)
                editor.apply()
                binding.detailStorageheart.setImageResource(R.drawable.onblackheart)
            }
        }

        binding.detailInfoMore.setOnClickListener {
            val intent = Intent(this, DetailInfoPopActivity::class.java)
            intent.putExtra("room_id", roomId)
            startActivity(intent)
        }

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(roomInfo.coordinate.lat, roomInfo.coordinate.lng))
        naverMap.moveCamera(cameraUpdate)


        val circle = CircleOverlay()
        circle.center = LatLng(roomInfo.coordinate.lat, roomInfo.coordinate.lng)
        circle.radius = 70.0
        circle.map = naverMap
        circle.color = Color.parseColor("#90B494")
    }

    fun setOptionLayout(option: Option): Int {
        var count = 0
        val setDefaultName :String = "detailOptionword"
        val setDefaultImage : String = "detailOption"
        var setName : String
        var setImage : String
        val bindingClass = binding::class.java

        if(option.bathroom == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "   욕실"
                onImage.setImageResource(R.drawable.bath)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.bed == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "   침실"
                onImage.setImageResource(R.drawable.bed)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.airConditioner == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "  에어컨"
                onImage.setImageResource(R.drawable.airconditioner)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.desk == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "   책상"
                onImage.setImageResource(R.drawable.desk)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.freeParking == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "무료 주차"
                onImage.setImageResource(R.drawable.parking)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.wifi == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "무선 인터넷"
                onImage.setImageResource(R.drawable.wifi)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.kitchen == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "주방 공유"
                onImage.setImageResource(R.drawable.kitchen)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.washer == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "세탁기 공유"
                onImage.setImageResource(R.drawable.washingmachine)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.elevator == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "엘리베이터"
                onImage.setImageResource(R.drawable.elevator)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.paidParking == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "유로 주차"
                onImage.setImageResource(R.drawable.parking)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.closet == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "   옷장"
                onImage.setImageResource(R.drawable.closet)
            }catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }

        if(option.tv == 1){
            count++
            setName = setDefaultName + count.toString()
            setImage = setDefaultImage + count.toString()
            try{
                val field = bindingClass.getDeclaredField(setName)
                val field2 = bindingClass.getDeclaredField(setImage)
                field.isAccessible = true
                field2.isAccessible = true
                val onName = field.get(binding) as TextView
                val onImage = field2.get(binding) as ImageView
                onName.text = "   TV"
                onImage.setImageResource(R.drawable.tv)
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
                    field.get(binding) as View
                offView.isGone = true
            } catch (e: NoSuchFieldException) {
                // Handle the case where the field doesn't exist
            } catch (e: IllegalAccessException) {
                // Handle any access-related exceptions
            }
        }
    }
}
