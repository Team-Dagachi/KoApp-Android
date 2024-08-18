package com.dagachi.koapp_android.viewmodel.main.learner.speaking.data

import com.dagachi.koapp_android.data.remote.model.ChatRole

/* 힌트 챗봇을 위한 모델 */
data class HintItem(
    val role: ChatRole,
    val message: String, // 전송할 메시지
    val hintMessage: String, // 받은 힌트 메시지
    val translatedHint: String // 받은 힌트 번역
)
