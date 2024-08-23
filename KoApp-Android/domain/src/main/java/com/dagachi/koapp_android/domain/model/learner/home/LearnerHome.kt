package com.dagachi.koapp_android.domain.model.learner.home

/* 학습자 홈 화면 엔티티 */
data class LearnerHome(
    val userName: String, // 사용자 이름
    val todayWord: String, // 오늘의 어휘
    val attendanceList: ArrayList<AttendanceItem>, // 이번주 출석률
    val missionList: ArrayList<MissionItem>, // 오늘의 미션 리스트
    val programList: ArrayList<String> // 프로그램 이름 리스트
)