package com.example.grandpa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        KakaoSdk.init(this, getString(R.string.kakao_native_key))

        val signupText: TextView = findViewById(R.id.login_signUp)
        signupText.setOnClickListener {
            val intent = Intent(this, SignupWithPhoneActivity::class.java)
            startActivity(intent)
            finish()
        }

        val kakaoLoginbtn: ImageButton = findViewById(R.id.btn_kakaoLogin)
        kakaoLoginbtn.setOnClickListener {
            SignupWithKakaoActivity.SignUpInfoDB.init(this)
            val editor = SignupWithKakaoActivity.SignUpInfoDB.getInstance().edit()
            editor.clear()
            editor.apply()

            //연동 초기화
            //kakaoUnlink()

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("LOGIN", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")

                    pushToken(token.accessToken) { userInfo, error ->
                        if (error != null) {
                            Log.e("PUSH_TOKEN", "토큰 전송 실패", error)
                        } else if (userInfo != null) {

                            Log.d("return Token", userInfo.toString())

                            if (userInfo is String) {
                                SignupLocationSchoolActivity.LoginTokenDB.init(this)
                                val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance().edit()
                                LoginTokenData.putString("accessToken", "Bearer $userInfo")
                                LoginTokenData.apply()

                                val intent = Intent(this, ShowRoomActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else if (userInfo is UserBigInfo) {
                                val intent = Intent(this, SignupWithKakaoActivity::class.java)
                                intent.putExtra("kakaoInfo", userInfo)
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                finish()
                            }
                        }
                    }
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->

                    if (error != null) {
                        Log.e("LOGIN", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i("LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")

                        pushToken(token.accessToken) { userInfo, error ->
                            if (error != null) {
                                Log.e("PUSH_TOKEN", "토큰 전송 실패", error)
                            } else if (userInfo != null) {

                                Log.d("return Token", userInfo.toString())

                                if (userInfo is String) {
                                    SignupLocationSchoolActivity.LoginTokenDB.init(this)
                                    val LoginTokenData = SignupLocationSchoolActivity.LoginTokenDB.getInstance().edit()
                                    LoginTokenData.putString("accessToken", "Bearer " + userInfo)
                                    LoginTokenData.apply()
                                    val intent = Intent(this, ShowRoomActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                } else if (userInfo is UserBigInfo) {
                                    val intent = Intent(this, SignupWithKakaoActivity::class.java)
                                    intent.putExtra("kakaoInfo", userInfo)
                                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                    finish()
                                }
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }

        }
    }

    private fun kakaoUnlink() {
        // 연결 끊기
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("Hello", "연결 끊기 실패", error)
            } else {
                Log.i("Hello", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
        finish()
    }

    private fun pushToken(token: String, callback: (Any, Throwable?) -> Unit){
        val service = AuthKaKaoLoginImpl.service_ct_tab
        val requestData = PushAccessAuth(token, "kakao") // 요청할 데이터 설정
        val callUrl = AUTH_URL + "login/"
        val call = service.sendDataToServer(callUrl, requestData) //post 함
        Log.d("requsetData", requestData.toString())


        call.enqueue(object : Callback<AnyAuthToken<Any>> {
            override fun onResponse(
                call: Call<AnyAuthToken<Any>>,
                response: Response<AnyAuthToken<Any>>
            ) {
                val statusCode = response.code() // 응답의 상태 코드를 가져옴
                Log.d("statusCode", statusCode.toString())

                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null){
                        val result = commonResponse.result

                        if(commonResponse.statusCode.toInt() == 200){
                            if (result is String) {
                                // 200 처리 (result는 String 형태)
                                callback(result , null)
                            } else {
                                // 처리할 수 없는 데이터 형식
                                Log.d("error", "올바르지 않은 데이터 형식")
                            }
                        }else if(commonResponse.statusCode.toInt() == 201){
                            if (result is LinkedTreeMap<*, *>) {
                                // 201 처리 (result는 UserResponse 형태)
                                val parsedData = Gson().fromJson(Gson().toJson(result), UserBigInfo::class.java)
                                callback(parsedData, null)
                            } else {
                                // 처리할 수 없는 데이터 형식
                                Log.d("error", "올바르지 않은 데이터 형식")
                            }
                        }
                    }
                } else {
                    // 서버가 오류 응답을 반환한 경우 처리하는 코드 추가
                    Log.d("error", "서버가 오류 응답 반환, 상태 코드: $statusCode")
                }
            }
            override fun onFailure(call: Call<AnyAuthToken<Any>>, t: Throwable) {
                // 네트워크 오류 발생 시 처리하는 코드 추가
                Log.e("Response", "Network error: ${t.message}")
            }
        })
    }
}