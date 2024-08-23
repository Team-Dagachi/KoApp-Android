package com.dagachi.koapp_android.config

import com.dagachi.koapp_android.di.ApplicationClass.Companion.X_AUTH_TOKEN
import com.dagachi.koapp_android.widget.utils.getJwt
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/*
 * API 통신 시 매번 호출
 * Access Token을 Header에 넣어주기 위한 클래스
 */
class XAcessTokenIntercepter: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val jwtToken: String? = getJwt()

        if (jwtToken != null) {
            builder.addHeader(X_AUTH_TOKEN, jwtToken)
        }

        return chain.proceed(builder.build())
    }
}