package com.dagachi.koapp_android.data.repository.remote.datasource.learner

import com.dagachi.koapp_android.data.remote.model.learner.home.LearnerHomeResponse
import retrofit2.Response

/*
* 학습자 홈 화면의 API 응답 상호작용을 관리하는 파일
* */
interface LearnerHomeDataSource {
    // 홈 화면 조회
    suspend fun getLearnerHome(): Response<LearnerHomeResponse>
}