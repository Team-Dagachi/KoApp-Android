package com.dagachi.koapp_android.widget.utils

import com.dagachi.koapp_android.di.ApplicationClass.Companion.X_AUTH_TOKEN
import com.dagachi.koapp_android.di.ApplicationClass.Companion.mSharedPreferences

// Jwt 토큰 저장
fun saveJwt(jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString(X_AUTH_TOKEN, jwtToken)
    editor.apply()
}

// Jwt 토큰 가져오기
fun getJwt(): String? = mSharedPreferences.getString(X_AUTH_TOKEN, null)

// Jwt 토큰 초기화
fun removeJwt() {
    val editor = mSharedPreferences.edit()
    editor.remove(X_AUTH_TOKEN)
    editor.apply()
}

// 모든 값 초기화
fun clearSpf() {
    val editor = mSharedPreferences.edit()
    editor.clear().apply()
}