package com.dagachi.koapp_android.view.main.learner.speaking

/* 말하기 연습의 주제 화면에서 사용되는 모델 */
data class SpeakingSubject(
    var isCheck: Boolean = false, // 학습 완료 여부
    val subject: String, // 타이틀
    val subjectKor: String? = null // 한국어
)