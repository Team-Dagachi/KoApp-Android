package com.dagachi.koapp_android.view.learner.home

/* 학습자의 홈 화면 주간 출석률에 들어갈 아이템 */
data class AttendanceItem(
    var day: String, // 요일
    var date: String, // 날짜
    var fullDate: String, // 년도-날짜-요일
    var isAttendance: Boolean,  // 출석 여부
    var isToday: Boolean // 오늘 날짜
)
