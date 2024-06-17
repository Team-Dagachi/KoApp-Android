package com.dagachi.koapp_android.view.main.learner.speaking

import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.data.remote.model.ChatMessage
import com.dagachi.koapp_android.data.remote.model.ChatRole
import com.dagachi.koapp_android.databinding.FragmentChattingBinding
import com.dagachi.koapp_android.view.base.BaseFragment
import com.dagachi.koapp_android.view.main.learner.speaking.adapter.ChattingAdapter
import com.dagachi.koapp_android.view.main.learner.speaking.listener.SpeakingRecognitionListener
import com.dagachi.koapp_android.view.main.learner.speaking.tts.DagachiTTS
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.ChattingViewModel
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.data.ChattingItem

/* Gemini 챗봇과의 주제별 말하기연습 스피킹 화면 */
class ChattingFragment : BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate) {
    private lateinit var viewModel: ChattingViewModel // 뷰모델
    private lateinit var speechRecognizer: SpeechRecognizer // STT 를 위한 sdk 객체
    private lateinit var systemInstruction: String // 프롬프트
    private lateinit var translateSystemInstruction: String // 번역용 프롬프트
    private lateinit var initMessage: String // 초기 메시지
    private lateinit var translatedInitMessage: String // 초기 번역 메시지

    private var chattingAdapter: ChattingAdapter = ChattingAdapter() // 채팅 어댑터
    private var messageList = mutableListOf<ChatMessage>() // 채팅 리스트
    private var dagachiTTS: DagachiTTS? = null // TTS 엔진
    private var isRecording = false

    override fun initViewCreated() {
        mainActivity!!.hideLearnerBottomNav(true)
        mainActivity!!.window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.Gray_100) // status bar 색상

        val args: ChattingFragmentArgs by navArgs()
        when (args.situationKorTitle) {
            // 학교(선생님, 친구)
            getString(R.string.speaking_school_teacher) -> {
                systemInstruction = getString(R.string.prompt_school_teacher) ?: ""
                initMessage = "요새 학교 생활은 어때요?"
                translatedInitMessage = "Cuộc sống học đường dạo này thế nào?"
            }

            getString(R.string.speaking_school_friend) -> {
                systemInstruction = getString(R.string.prompt_school_friend) ?: ""
                initMessage = "내일 챙겨와야 하는 준비물이 뭐야?" ?: ""
                translatedInitMessage = "Ngày mai tôi cần mang theo những gì?"
            }
            // 미디어와 콘텐츠(k-pop, 드라마, 게임)
            getString(R.string.speaking_media_and_content_kpop) -> {
                systemInstruction = getString(R.string.prompt_media_and_content_kpop) ?: ""
                initMessage = "혹시 좋아하는 K-POP 가수 있어요?"
                translatedInitMessage = "Bạn có ca sĩ K-POP yêu thích nào không?"
            }

            getString(R.string.speaking_media_and_content_kdrama) -> {
                systemInstruction = getString(R.string.prompt_media_and_content_kdrama) ?: ""
                initMessage = "혹시 좋아하는 한국 드라마 있어요?"
                translatedInitMessage = "Bạn có bộ phim truyền hình Hàn Quốc yêu thích nào không?"
            }

            getString(R.string.speaking_media_and_content_game) -> {
                systemInstruction = getString(R.string.prompt_media_and_content_game) ?: ""
                initMessage = "혹시 좋아하는 게임 있어요?"
                translatedInitMessage = "Bạn có trò chơi yêu thích nào không?"
            }
        }

        translateSystemInstruction = getString(R.string.prompt_translate_vietnam) ?: ""

        // 툴바 제목 설정
        setToolbarTitle(binding.toolbarChatting.tvSubToolbarTitle, args.situationKorTitle)
    }

    override fun initAfterBinding() {
        // 뒤로가기 버튼 클릭 이벤트
        binding.toolbarChatting.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        viewModel = ViewModelProvider(
            this,
            ChattingViewModel.ChattingViewModelFactory(
                systemInstruction,
                translateSystemInstruction
            )
        )[ChattingViewModel::class.java]

        // 초기 질문 메시지 추가
        messageList.add(ChatMessage(ChatRole.MODEL, initMessage, translatedInitMessage))

        binding.rvChatting.adapter = chattingAdapter // 어댑터 연결
        chattingAdapter.setMessage(messageList) // 초기 질문 넣기

        binding.iBtnFragmentChattingMic.setOnClickListener {
            startListening(getRecognitionListener())
        }

        dagachiTTS = DagachiTTS.getInstance(requireContext(), TextToSpeech.OnInitListener {
            initMessage?.let {
                dagachiTTS?.textToSpeech(it, false)
            }
        }) // tts 엔진 초기화

        observeMessage() // 메시지 받기
    }

    private fun startListening(listener: RecognitionListener) {
        if (!isRecording) {
            dagachiTTS?.stop()
            val recordingIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireContext().packageName)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            }
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext()).apply {
                setRecognitionListener(listener)
                startListening(recordingIntent)
            }
        }
    }

    private fun showRecognizerErrorToast(recognizerError: Int) {
        val errorMessage = when (recognizerError) {
            SpeechRecognizer.ERROR_AUDIO -> "오디오 레코딩 오류"
            SpeechRecognizer.ERROR_CLIENT -> "디바이스 관련 오류"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "권한 오류"
            SpeechRecognizer.ERROR_NETWORK -> "네트워크 관련 오류"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트워크 응답 시간 초과"
            SpeechRecognizer.ERROR_NO_MATCH -> "결과 불러오기 실패"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "녹음에 실패 하였습니다."
            else -> "잠시 후 다시 시도 해주세요"
        }

        setButtonNormalState()
        showToast(errorMessage, Toast.LENGTH_LONG)
        isRecording = false
    }

    private fun setButtonNormalState() {
        binding.iBtnFragmentChattingMic.setImageResource(R.drawable.ic_mic_36)
        binding.iBtnFragmentChattingMic.visibility = View.VISIBLE
        binding.iBtnFragmentChattingRecAnimation.visibility = View.GONE
        binding.tvFragmentChattingRecTooltip.visibility = View.GONE
    }

    private fun getRecognitionListener(): RecognitionListener =
        binding.tvFragmentChattingRecTooltip.run {
            val button = binding.iBtnFragmentChattingMic
            SpeakingRecognitionListener(
                {
                    binding.iBtnFragmentChattingMic.visibility = View.GONE
                    binding.iBtnFragmentChattingRecAnimation.visibility = View.VISIBLE
                    text = getString(R.string.speaking_recording_tooltip)
                    visibility = View.VISIBLE
                },
                {
                    button.setImageResource(R.drawable.ic_loading_36)
                    text = getString(R.string.speaking_recording_analysis)
                    visibility = View.VISIBLE
                },
                {
                    /**
                     * STT 결과 전송 및 UI 처리
                     */
                    runCatching {
                        val userMessage =
                            it?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                                ?.joinToString("") ?: ""
                        if (userMessage.isNotEmpty()) {
                            viewModel.geminiChat(userMessage)
                        }
                        isRecording = false
                    }.onFailure {
                        showRecognizerErrorToast(-1) // 결과 전송중 오류 이므로 -1 로 보냅니다.
                    }
                },
                {
                    showRecognizerErrorToast(it)
                },
            )
        }

    private fun getChattingResponseObserver(role: ChatRole): Observer<ChattingItem?> =
        Observer<ChattingItem?> {
            it?.let { response ->
                messageList.add(
                    ChatMessage(
                        role,
                        response.message.trim(),
                        response.translatedMessage.trim()
                    )
                )
                chattingAdapter.setMessage(messageList)
                scrollToBottom()

                when (role) {
                    ChatRole.MODEL -> {
                        dagachiTTS?.textToSpeech(response.message, false)
                    }

                    else -> {}
                }

                setButtonNormalState()
            }
        }

    // 메시지 받기
    private fun observeMessage() {
        viewModel.modelChattingResponse.observe(
            viewLifecycleOwner,
            getChattingResponseObserver(ChatRole.MODEL)
        )
        viewModel.userChattingResponse.observe(
            viewLifecycleOwner,
            getChattingResponseObserver(ChatRole.USER)
        )
    }

    // 채팅 목록 하단으로 스크롤
    private fun scrollToBottom() {
        binding.rvChatting.smoothScrollToPosition(chattingAdapter.itemCount - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        dagachiTTS?.close()
        dagachiTTS = null
    }
}
