package com.dagachi.koapp_android.viewmodel.main.learner.speaking.data

/* 피드백 챗봇을 위한 모델 */
data class FeedbackItem(
    val message: String, // 전송할 메시지
    val feedbackMessage: String? = null, // 피드백 문장
    val feedbackReason: String? = null, // 피드백 이유
    val translatedFeedbackReason: String? = null // 피드백 이유 번역
)
