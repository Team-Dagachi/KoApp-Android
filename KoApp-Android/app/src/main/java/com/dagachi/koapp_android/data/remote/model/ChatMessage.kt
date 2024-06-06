package com.dagachi.koapp_android.data.remote.model

/* 챗봇 제작에 필요한 모델 */
// 챗봇 역할
enum class ChatRole {
    MODEL, // 모델
    USER, // 사용자
    ERROR // 에러
}

// 챗봇 모델
data class ChatMessage(
    val role: ChatRole, // 역할
    var message: String // 메시지
)