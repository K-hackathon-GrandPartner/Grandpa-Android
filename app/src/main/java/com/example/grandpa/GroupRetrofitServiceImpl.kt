package com.example.grandpa

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//room 모두 가져오는 싱글톤 패턴 object 구현
object ShowRoomImpl {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //인증탭 - 메인
    val service_ct_tab : ShowRoomInterface = retrofit.create(ShowRoomInterface::class.java)
}

//room의 해당 id만 가져오는 object
object DetailRoomImpl{

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //인증탭 - 메인
    val service_ct_tab : DetailRoomInterface = retrofit.create(DetailRoomInterface::class.java)
}