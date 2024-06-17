package com.dagachi.koapp_android.data.remote.model

/* 챗봇 제작에 필요한 모델 */
// 챗봇 역할
enum class ChatRole {
    MODEL, // 모델
    USER, // 사용자
    HINT // 힌트
}

// 챗봇 모델
data class ChatMessage(
    val role: ChatRole, // 역할
    var message: String, // 기본 메시지
    var translateMessage: String? = "", // 기본 메시지 번역
    var isShowTranslation: Boolean = false, // translate 메시지 보임 여부
    var ttsCount: Int = 0, // 기본 말풍선의 tts 를 사용한 횟수
    var feedbackMessage: String? = null, // 피드백 문장
    var feedbackReason: String? = null, // 피드백 이유
    var translatedFeedbackReason: String? = null, // 피드백 이유 번역
    var feedbackTTSCount: Int = 0, // 피드백 말풍선의 tts를 사용한 횟수
    var hintMessage: String? = null, // 받은 힌트 메시지
    var translatedHint: String? = null, // 받은 힌트 번역
    var hintTTSCount: Int = 0, // 힌트 말풍선의 tts를 사용한 횟수
)
