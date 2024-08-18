package com.dagachi.koapp_android.view.main.learner.speaking

/* 말하기 연습의 상황별 화면에서 사용되는 모델 */
data class SpeakingSituation(
    val situationTip: String, // 대화 팁
    val situationKor: String? = null // 한국어
)
