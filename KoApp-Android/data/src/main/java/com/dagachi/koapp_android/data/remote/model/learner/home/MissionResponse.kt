package com.dagachi.koapp_android.data.remote.model.learner.home

/* 학습자 홈 화면의 오늘의 미션 API 응답 데이터 모델 */
data class MissionResponse(
    val missionType: Int, // 미션 유형
    val missionName: String, // 미션 이름
    val isCompletedMission: Boolean, // 미션 진행 상태
    val missionDate: String?, // 미션 기한
    val missionProgress: Int? // 미션 진행률
)