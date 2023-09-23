package com.example.grandpa

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient

class LoginActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val signupText: TextView = findViewById(R.id.login_signUp)
        signupText.setOnClickListener {
            val intent = Intent(this, SignupWithPhoneActivity::class.java)
            startActivity(intent)
            finish()
        }


        // 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            }
            else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SignupWithKakaoActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        val kakaoLoginbtn: ImageButton = findViewById(R.id.btn_kakaoLogin)
        kakaoLoginbtn.setOnClickListener {
            HandleKakaoLogin()
        }
    }

    fun HandleKakaoLogin() {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            Log.d("iskakaoTalkLoginAvailable","true")
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            Log.d("iskakaoTalkLoginAvailable","false")
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        Log.d("callback 함수","실행")
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error.cause)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

            // 카카오 로그인 성공 시 원하는 동작 수행
            moveToNextActivity()
        }
    }

    // 카카오 로그인 성공 시 원하는 동작을 수행하는 메서드
    private fun moveToNextActivity() {
        // 로그인 성공 시 다음 액티비티로 이동 (예시: MainActivity)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // 현재 액티비티 종료 (선택적)
    }
}


