package com.dagachi.koapp_android.view.main.learner.speaking.listener

import android.os.Bundle
import android.speech.RecognitionListener

open class SpeakingRecognitionListener(
    private val _onReadyForSpeech: (params: Bundle?) -> Unit,
    private val _onEndOfSpeech: () -> Unit,
    private val _onResults: (results: Bundle?) -> Unit,
    private val _onError: ((error: Int) -> Unit),
) : RecognitionListener {
    override fun onReadyForSpeech(params: Bundle?) {
        _onReadyForSpeech(params)
    }

    /**
     * 녹음이 준비되거나, 사용자가 말을 시작해도 UI 가 같으므로,
     * onBeginningOfSpeech 에서는 아무것도 하지 않습니다.
     */
    override fun onBeginningOfSpeech() {}

    /**
     * 입력받는 목소리의 크기는 신경쓰지 않습니다.
     */
    override fun onRmsChanged(rmsdB: Float) {}

    /**
     * 녹음이 완료되기 전까지는 대기하므로 아무것도 하지 않습니다.
     */
    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {
        _onEndOfSpeech()
    }

    override fun onError(error: Int) {
        _onError(error)
    }

    override fun onResults(results: Bundle?) {
        _onResults(results)
    }

    /**
     * 부분 인식 결과는 사용하지 않습니다.
     */
    override fun onPartialResults(partialResults: Bundle?) {}

    /**
     * 이벤트 추가시 사용하는데, 저희는 사용할일이 없습니다.
     */
    override fun onEvent(eventType: Int, params: Bundle?) {}
}