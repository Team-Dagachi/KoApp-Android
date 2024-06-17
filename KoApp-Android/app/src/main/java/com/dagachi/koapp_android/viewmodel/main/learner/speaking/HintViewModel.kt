package com.dagachi.koapp_android.viewmodel.main.learner.speaking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dagachi.koapp_android.BuildConfig
import com.dagachi.koapp_android.data.remote.model.ChatRole
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.data.HintItem
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

/* Gemini 힌트 챗봇 ViewModel */
class HintViewModel(systemInstruction: String, translateSystemInstruction: String): ViewModel() {
    private var _hintResponse = MutableLiveData<HintItem>()
    val hintResponse: LiveData<HintItem> get() = _hintResponse
    val isLoading = MutableLiveData<Boolean>() // 힌트 로딩 여부

    private val hintGeminiChat: Chat // 힌트
    private val translationGeminiChat: Chat // 힌트 번역

    init {
        hintGeminiChat = createGeminiChat(systemInstruction)
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
                maxOutputTokens = 50
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
                    // 힌트 요청 및 응답
                    val hintResponse = hintGeminiChat.sendMessage(message).text ?: ""

                    Log.e("힌트 테스트", hintResponse)

                    // 힌트에 대한 번역 요청
                    val translatedHintResponse = translationGeminiChat.sendMessage(hintResponse).text ?: ""

                    // 응답 저장
                    _hintResponse.value = HintItem(
                        ChatRole.HINT,
                        message,
                        hintResponse.trim(),
                        translatedHintResponse.trim()
                    )
                } catch (e: Exception) {
                    Log.e("HintViewModel", e.stackTraceToString())
                } finally {
                    isLoading.value = false
                }
            }.onFailure {
                Log.e("HintViewModel", it.stackTraceToString())
            }
        }
    }

    // 뷰모델 설정
    class HintViewModelFactory(
        private val systemInstruction: String,
        private val translateSystemInstruction: String
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HintViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HintViewModel(systemInstruction, translateSystemInstruction) as T
            }
            throw IllegalArgumentException("Unknown Hint ViewModel class")
        }
    }
}