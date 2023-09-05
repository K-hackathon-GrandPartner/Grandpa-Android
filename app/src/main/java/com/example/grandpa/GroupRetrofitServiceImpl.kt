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
    private val retrofit: Retrofit = Retrofit.Builder() //http요청을 만들고 서버와 통신
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) //json데이터를 kotlin 객체로 변환하기 위한 converter
        .build()

    //인증탭 - 메인 Retrofit 인터페이스를 사용하여 서비스 인스턴스를 생성
    val service_ct_tab : DetailRoomInterface = retrofit.create(DetailRoomInterface::class.java)
}