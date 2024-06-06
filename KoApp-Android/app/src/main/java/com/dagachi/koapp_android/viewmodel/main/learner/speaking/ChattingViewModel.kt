package com.dagachi.koapp_android.viewmodel.main.learner.speaking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.launch

/* Gemini 챗봇 ViewModel */
class ChattingViewModel(geminiFlash: GenerativeModel): ViewModel() {
    private var _chattingResponse = MutableLiveData<GenerateContentResponse>()
    val chattingResponse: LiveData<GenerateContentResponse> get() = _chattingResponse
    val isLoading = MutableLiveData<Boolean>() // 채팅 로딩 여부

    private val geminiChat: Chat = geminiFlash.startChat()

    fun geminiChat(message: String) {
        isLoading.value = true

        viewModelScope.launch {
            try {
                _chattingResponse.value = geminiChat.sendMessage(message)
            } catch (e: Exception) {
                Log.e("ChattingViewModel", e.stackTraceToString())
            } finally {
                isLoading.value = false
            }

        }
    }

    class ChattingViewModelFactory(private val geminiFlash: GenerativeModel): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChattingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChattingViewModel(geminiFlash) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}