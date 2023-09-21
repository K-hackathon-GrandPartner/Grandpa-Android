package com.example.grandpa

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        //네이티브 앱 키를 사용하여 kakao sdk 초기화
        KakaoSdk.init(this, getString(R.string.kakao_native_key))
    }
}