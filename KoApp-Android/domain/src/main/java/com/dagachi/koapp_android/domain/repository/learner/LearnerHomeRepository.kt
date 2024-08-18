package com.dagachi.koapp_android.domain.repository.learner

import com.dagachi.koapp_android.domain.model.learner.home.LearnerHome

/* 학습자 홈 화면의 레포지토리 */
interface LearnerHomeRepository {
    // 홈 화면 조회
    suspend fun getLearnerHome(): LearnerHome
}