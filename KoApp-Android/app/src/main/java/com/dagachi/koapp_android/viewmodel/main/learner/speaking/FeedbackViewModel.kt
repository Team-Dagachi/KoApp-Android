package com.dagachi.koapp_android.viewmodel.main.learner.speaking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dagachi.koapp_android.BuildConfig
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.data.FeedbackItem
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

/* Gemini 피드백 챗봇 ViewModel */
class FeedbackViewModel(systemInstruction: String, translateSystemInstruction: String): ViewModel() {
    private var _feedbackChattingResponse = MutableLiveData<FeedbackItem>()
    val feedbackChattingResponse: LiveData<FeedbackItem> get() = _feedbackChattingResponse
    val isLoading = MutableLiveData<Boolean>() // 채팅 로딩 여부

    private val feedbackGeminiChat: Chat // 피드백
    private val translationGeminiChat: Chat // 번역

    init {
        feedbackGeminiChat = createGeminiChat(systemInstruction)
        translationGeminiChat = createGeminiChat(translateSystemInstruction)
    }

    // Gemini API 연결
    private fun createGeminiChat(instruction: String): Chat {
       return GenerativeModel(
            "gemini-1.5-flash",
            BuildConfig.GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 1f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 100
                responseMimeType = "text/plain"
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH),
            ),
            systemInstruction = content { text(instruction) },
        ).run {
            startChat()
       }
    }

    // 챗봇 답장 연결
    fun geminiChat(message: String) {
        isLoading.value = true

        viewModelScope.launch {
            runCatching {
                try {
                    // 피드백 요청 및 응답
                    val feedbackResponse = feedbackGeminiChat.sendMessage(message).text ?: ""
                    // 피드백을 리스트로 분리
                    val feedbackList = feedbackResponse.split(";")

                    Log.e("피드백 테스트", feedbackResponse)
                    Log.e("피드백 테스트", feedbackList.toString())

                    // 응답이 0으로 왔다면, 자연스러운 문장
                    if (feedbackList[0] == "0" || feedbackResponse == "0;") {
                        // 응답 저장
                        _feedbackChattingResponse.value = FeedbackItem(message)
                    }
                    // 응답이 0으로 안 왔다면, 피드백을 해준 문장
                    else {
                        // 피드백에 대한 번역 요청
                        val translatedFeedbackResponse = translationGeminiChat.sendMessage(feedbackList[2]).text ?: ""

                        // 응답 저장
                        _feedbackChattingResponse.value = FeedbackItem(
                            message,
                            feedbackList[1].trim(),
                            feedbackList[2].trim(),
                            translatedFeedbackResponse.trim()
                        )
                    }
                } catch (e: Exception) {
                    Log.e("FeedbackViewModel", e.stackTraceToString())
                } finally {
                    isLoading.value = false
                }
            }.onFailure {
                Log.e("FeedbackViewModel", it.stackTraceToString())
            }
        }
    }

    // 뷰모델 설정
    class FeedbackViewModelFactory(
        private val systemInstruction: String,
        private val translateSystemInstruction: String
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FeedbackViewModel(systemInstruction, translateSystemInstruction) as T
            }
            throw IllegalArgumentException("Unknown Feedback ViewModel class")
        }
    }
}