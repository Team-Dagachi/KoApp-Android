package com.dagachi.koapp_android.view.main.learner.speaking.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class DagachiTTS private constructor(context: Context, listener: OnInitListener?) :
    TextToSpeech(context, listener) {
    init {
        setPitch(1.0f)
        setSpeechRate(1.0f)
        language = Locale.KOREA
    }

    fun textToSpeech(text: String, isTTSButton: Boolean = true) {
        val addFlag = if (isTTSButton) {
            TextToSpeech.QUEUE_FLUSH
        } else {
            TextToSpeech.QUEUE_ADD
        }
        if (isTTSButton) {
            stop()
        }

        speak(text, addFlag, null, "null")
    }

    fun close() {
        stop()
        shutdown()
        instance = null
    }

    companion object {
        private var instance: DagachiTTS? = null

        fun getInstance(context: Context, listener: OnInitListener? = null): DagachiTTS {
            return instance ?: synchronized(this) {
                instance ?: DagachiTTS(context, listener).also {
                    instance = it
                }
            }
        }
    }
}