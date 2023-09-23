package com.example.grandpa

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url


//room 모두 가져오는 인터페이스
interface ShowRoomInterface {
    @GET()
    fun requestList(@Url url: String, @Header("accept") accept: String): Call<ShowRoomResponse>
}

//room의 해당 id만 가지고 오는 인터페이스
interface DetailRoomInterface{
    @GET()
    fun requestList(@Url url: String): Call<DetailRoomResponse>
}

// 필터링한 데이터 가져오는 인터페이스
interface FilteredRoomInterface {
    @GET(BASE_URL)
    fun requestList(
        @QueryMap queryMap: Map<String, Float>,
        @Query("regions") regions: List<String>,
        @Query("buildingTypes") buildingTypes: List<String>,
        @Query("roomSizeTypes") roomSizeTypes: List<String>,
        @Query("roomOptions") roomOptions: List<String>
    ): Call<ShowRoomResponse>
}

interface KakaoLoginInterface{
    @POST()
    fun sendDataToServer(@Url url: String, @Body requestData: PushAccessAuth): Call<AuthToken>
}

interface KakaoSignUpInterface {
    @FormUrlEncoded
    @POST(AUTH_URL + "register/")
    fun postSignUpInfo(@FieldMap requestData: HashMap<String, Any>): Call<SignUpToken>
}




