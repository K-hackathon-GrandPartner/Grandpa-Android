package com.example.grandpa

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//room 모두 가져오는 싱글톤 패턴 object 구현
object ShowRoomImpl {
//    const val BASE_URL = "http://dongho18.iptime.org:3000/api/room/" //여기 주소 무조건 /로 끝나야 함
    const val BASE_URL = "http://35.216.98.240:3000/api/room/"
    //retrofit 객체 생성
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //인증탭 - 메인
    val service_ct_tab : ShowRoomInterface = retrofit.create(ShowRoomInterface::class.java)
}

//room의 해당 id만 가져오는 object
object DetailRoomImpl{
    //const val BASE_URL = "http://dongho18.iptime.org:3000/api/room/"
    const val BASE_URL = "http://35.216.98.240:3000/api/room/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ShowRoomImpl.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //인증탭 - 메인
    val service_ct_tab : DetailRoomInterface = retrofit.create(DetailRoomInterface::class.java)
}