package com.dagachi.koapp_android.remote.model

/* 학습자의 홈 화면에 사용하는 데이터 모델 */
// 홈 화면에서 사용하는 기본 모델
data class LearnerHomeModel(
    val todayWord: String, // 오늘의 어휘
    var attendanceList: ArrayList<Boolean>, // 이번주 출석률
    var todayMissionList: ArrayList<TodayMissionModel>, // 오늘의 미션
    var programList: ArrayList<String> // 참여중인 프로그램
)

// 오늘의 미션 모델
data class TodayMissionModel(
    var missionName: String, // 미션 이름
    var missionDate: String?, // 미션 기한
    var missionStatus: Boolean, // 미션 상태(미션 전, 미션 완료)
    var progress: Int? // 미션 진행률(
)