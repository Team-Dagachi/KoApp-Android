package com.dagachi.koapp_android.data.remote.model.learner.home

import com.google.gson.annotations.SerializedName

/* 학습자 홈 화면의 API 응답 데이터 모델 */
data class LearnerHomeResponse(
    @SerializedName("userName") val userName: String, // 사용자 이름
    @SerializedName("todayWord") val todayWord: String, // 오늘의 어휘
    @SerializedName("attendanceList") val attendanceList: ArrayList<Boolean>, // 이번주 출석률
    @SerializedName("missionList") val missionList: ArrayList<MissionResponse>, // 오늘의 미션 리스트
    @SerializedName("programList") val programList: ArrayList<String> // 프로그램 이름 리스트
)