package com.example.grandpa

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GroupRetrofitServiceImpl {
    const val BASE_URL = "http://dongho18.iptime.org:3000/api/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //인증탭 - 메인
    val service_ct_tab : CertifRetrofitService = retrofit.create(CertifRetrofitService::class.java)
}