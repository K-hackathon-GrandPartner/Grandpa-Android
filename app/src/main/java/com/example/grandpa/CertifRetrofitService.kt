package com.example.grandpa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface CertifRetrofitService {
    @GET
    fun requestList(@Url url: String): Call<List<Data>>
}