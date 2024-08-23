package com.dagachi.koapp_android.data.remote.api.learner.home

import com.dagachi.koapp_android.data.remote.model.learner.home.LearnerHomeResponse
import retrofit2.Response
import retrofit2.http.GET

/* 학습자 홈 화면의 API 요청 인터페이스 */
interface LearnerHomeApi {
    // 홈 화면 조회
    @GET("/api/home")
    suspend fun getLearnerHome(): Response<LearnerHomeResponse>
}