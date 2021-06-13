package com.example.ez_tour

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "61c8188e57dfd2ad836236244c72f0e4")

    }
}