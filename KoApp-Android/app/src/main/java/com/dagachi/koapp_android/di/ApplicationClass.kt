package com.dagachi.koapp_android.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.dagachi.koapp_android.BuildConfig
import dagger.hilt.android.HiltAndroidApp

/* 공통적으로 사용하는 데이터를 관리하는 파일 */
@HiltAndroidApp
class ApplicationClass: Application() {
    companion object {
        private lateinit var instance: ApplicationClass

        const val X_AUTH_TOKEN: String = "Authorization" // token 키 값
        const val TAG: String = "KO-APP" // SharedPreferences 키 값

        private const val DEV_URL: String = "http://10.0.2.2:8080" // 테스트 주소
        private const val PROD_URL: String = BuildConfig.BASE_URL // 실서버 주소
        const val BASE_URL: String = DEV_URL // 서버를 번갈아 가며 테스트하기 위한 변수

        lateinit var mSharedPreferences: SharedPreferences

        // context 가져오기
        fun applicationContext(): Application {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}