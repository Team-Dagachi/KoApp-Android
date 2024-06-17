package com.dagachi.koapp_android.viewmodel.main.learner.speaking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dagachi.koapp_android.BuildConfig
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.data.ChattingItem
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

/* Gemini 챗봇 ViewModel */
class ChattingViewModel(systemInstruction: String, translateSystemInstruction: String) :
    ViewModel() {
    private var _modelChattingResponse = MutableLiveData<ChattingItem>()
    private var _userChattingResponse = MutableLiveData<ChattingItem>()
    val modelChattingResponse: LiveData<ChattingItem> get() = _modelChattingResponse
    val userChattingResponse: LiveData<ChattingItem> get() = _userChattingResponse
    val isLoading = MutableLiveData<Boolean>() // 채팅 로딩 여부

    private val geminiChat: Chat
    private val translationGeminiChat: Chat

    init {
        geminiChat = createGeminiChat(systemInstruction)
        translationGeminiChat = createGeminiChat(translateSystemInstruction)
    }

    private fun createGeminiChat(instruction: String): Chat = GenerativeModel(
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
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
        ),
        systemInstruction = content { text(instruction) },
    ).run {
        startChat()
    }

    fun geminiChat(message: String) {
        isLoading.value = true

        viewModelScope.launch {
            runCatching {
                // 먼저 user message 에 대한 번역 요청
                val translatedUserMessage = translationGeminiChat.sendMessage(message).text ?: ""
                _userChattingResponse.value = ChattingItem(message, translatedUserMessage)

                // 이제 user message 에 대한 gemini 답 요청
                val modelResponse = geminiChat.sendMessage(message).text ?: ""
                val translatedModelResponse = translationGeminiChat.sendMessage(modelResponse).text ?: ""

                _modelChattingResponse.value = ChattingItem(modelResponse, translatedModelResponse)
            }.onFailure {
                Log.e("ChattingViewModel", it.stackTraceToString())
            }

            isLoading.value = false
        }
    }

    class ChattingViewModelFactory(
        private val systemInstruction: String,
        private val translateSystemInstruction: String
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChattingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChattingViewModel(systemInstruction, translateSystemInstruction) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
