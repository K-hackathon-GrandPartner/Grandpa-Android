package com.example.grandpa

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient


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
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("LOGIN", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")

                    updateKakaoLoginUi()
                    val intent = Intent(this, SignupWithKakaoActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
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
                        updateKakaoLoginUi()
                        val intent = Intent(this, SignupWithKakaoActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

    }

    private fun updateKakaoLoginUi() {
        // 지금 birthday 부분 agreed=false 되어 있음
        // 추가 동의 하는 방법 찾아야 할 듯
        UserApiClient.instance.scopes { scopeInfo, error->
            if (error != null) {
                Log.e(TAG, "동의 정보 확인 실패", error)
            } else if (scopeInfo != null) {
                Log.i(TAG, "동의 정보 확인 성공\n 현재 가지고 있는 동의 항목 $scopeInfo")
            }
        }

        // 로그인 여부에 따른 UI 설정
        UserApiClient.instance.me { user, throwable ->
            if (user != null) {
                // 유저의 닉네임
                Log.d(TAG, "invoke: nickname = ${user.kakaoAccount?.profile?.nickname}")
                // 유저의 성별
                Log.d(TAG, "invoke: gender = ${user.kakaoAccount?.gender}")
                // 유저의 연령대
                Log.d(TAG, "invoke: age = ${user.kakaoAccount?.birthday}")
                Log.d(TAG, "invoke: birthyear = ${user.kakaoAccount?.birthyear}")
            }
        }
    }

}


