package com.dagachi.koapp_android.view.main.learner.speaking

import android.annotation.SuppressLint
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
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
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.FeedbackViewModel
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.HintViewModel
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.data.ChattingItem
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.data.FeedbackItem
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.data.HintItem

/* Gemini 챗봇과의 주제별 말하기연습 스피킹 화면 */
class ChattingFragment : BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate) {
    private lateinit var viewModel: ChattingViewModel // 채팅 뷰모델
    private lateinit var feedbackViewModel: FeedbackViewModel // 피드백 뷰모델
    private lateinit var hintViewModel: HintViewModel // 힌트 뷰모델

    private lateinit var systemInstruction: String // 프롬프트
    private lateinit var translateSystemInstruction: String // 번역용 프롬프트
    private lateinit var hintSystemInstruction: String // 힌트 프롬프트
    private lateinit var initMessage: String // 초기 메시지
    private lateinit var translatedInitMessage: String // 초기 번역 메시지

    private var chattingAdapter: ChattingAdapter = ChattingAdapter() // 채팅 어댑터
    private var messageList = mutableListOf<ChatMessage>() // 채팅 리스트
    private lateinit var speechRecognizer: SpeechRecognizer // STT 를 위한 sdk 객체
    private var dagachiTTS: DagachiTTS? = null // TTS 엔진
    private var isRecording = false // 녹음중 여부

    private var isShowHint: Boolean = false // 힌트 버튼 클릭 여부
    private var hintMessageList = ArrayList<String>() // 힌트 메시지 리스트

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
                hintSystemInstruction = getString(R.string.prompt_hint_teacher) ?: ""
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
                hintSystemInstruction = getString(R.string.prompt_hint_kdrama) ?: ""
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

    @SuppressLint("NotifyDataSetChanged")
    override fun initAfterBinding() {
        // 뒤로가기 버튼 클릭 이벤트
        binding.toolbarChatting.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 채팅 뷰모델 연결
        viewModel = ViewModelProvider(
            this,
            ChattingViewModel.ChattingViewModelFactory(
                systemInstruction,
                translateSystemInstruction
            )
        )[ChattingViewModel::class.java]

        // 피드백 뷰모델 연결
        feedbackViewModel = ViewModelProvider(
            this,
            FeedbackViewModel.FeedbackViewModelFactory(
                getString(R.string.prompt_feedback),
                translateSystemInstruction
            )
        )[FeedbackViewModel::class.java]

        // 힌트 뷰모델 연결
        hintViewModel = ViewModelProvider(
            this,
            HintViewModel.HintViewModelFactory(
                hintSystemInstruction,
                getString(R.string.prompt_translate_korean)
            )
        )[HintViewModel::class.java]

        // 초기 질문 메시지 추가
        messageList.add(ChatMessage(ChatRole.MODEL, initMessage, translatedInitMessage))

        binding.rvChatting.adapter = chattingAdapter // 어댑터 연결
        chattingAdapter.setMessage(ChatMessage(ChatRole.MODEL, initMessage, translatedInitMessage)) // 초기 질문 넣기

        // 마이크 버튼 클릭 이벤트
        binding.iBtnFragmentChattingMic.setOnClickListener {
            startListening(getRecognitionListener())
        }

        // 힌트 버튼 클릭 이벤트
        binding.btnChattingHint.setOnClickListener {
            // 힌트 숨기기
            if (isShowHint) {
                binding.btnChattingHint.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.Orange_Medium)
                binding.btnChattingHint.text = "힌트 보기"

                chattingAdapter.changeHintView(true,
                    ChatMessage(
                        role = ChatRole.HINT,
                        message = hintMessageList[0],
                        hintMessage = hintMessageList[1],
                        translatedHint = hintMessageList[2],
                    )
                )
            }
            // 힌트 보이기
            else {
                binding.btnChattingHint.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.Gray_500)
                binding.btnChattingHint.text = "힌트 숨기기"

                // 리스트의 가장 마지막에 있는 힌트 값 가져오기
                //val hintMessage: ChatMessage? = messageList.findLast { it.role == ChatRole.HINT }

                // 기존에 힌트가 있다면
                if (hintMessageList.isNotEmpty()) {
                    chattingAdapter.changeHintView(false,
                        ChatMessage(
                            role = ChatRole.HINT,
                            message = hintMessageList[0],
                            hintMessage = hintMessageList[1],
                            translatedHint = hintMessageList[2],
                        )
                    )
                }
                // 없다면
                else {
                    // 리스트의 가장 마지막에 있는 모델 값 가져오기
                    val message: ChatMessage? = messageList.findLast { it.role == ChatRole.MODEL }

                    if (message != null) {
                        // 힌트 챗봇 연결
                        hintViewModel.geminiChat(message.message)
                    }
                }
            }

            // 변수 상태 변경
            isShowHint = !isShowHint
        }

        // tts 엔진 초기화
        dagachiTTS = DagachiTTS.getInstance(requireContext(), TextToSpeech.OnInitListener {
            initMessage.let {
                dagachiTTS?.textToSpeech(it, false)
            }
        })

        observeMessage() // 채팅 메시지 받기
    }

    // 사용자의 목소리 녹음 시작
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

    // stt 관련 토스트 메시지
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

    // mic 버튼 기본 상태(녹음 중 애니메이션 숨기기)
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
                    // 녹음중 애니메이션 띄우기
                    binding.iBtnFragmentChattingMic.visibility = View.INVISIBLE
                    binding.iBtnFragmentChattingRecAnimation.visibility = View.VISIBLE
                    text = getString(R.string.speaking_recording_tooltip)
                    visibility = View.VISIBLE
                },
                {
                    // 로딩 이미지 띄우기
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
                            // 힌트가 채팅 리스트에 있다면
                            if (chattingAdapter.getHintInList()) {
                                // 힌트 숨기기
                                chattingAdapter.changeHintView(true,
                                    ChatMessage(
                                        role = ChatRole.HINT,
                                        message = hintMessageList[0],
                                        hintMessage = hintMessageList[1],
                                        translatedHint = hintMessageList[2],
                                    )
                                )
                            }

                            // 변수 상태 변경
                            binding.btnChattingHint.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.Orange_Medium)
                            binding.btnChattingHint.text = "힌트 보기"
                            isShowHint = false
                            hintMessageList.clear() // 힌트 초기화

                            viewModel.geminiChat(userMessage) // 채팅 모델 연결
                            feedbackViewModel.geminiChat(userMessage) // 피드백 모델 연결
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

    // 채팅 챗봇 답장 관찰하기
    private fun getChattingResponseObserver(role: ChatRole): Observer<ChattingItem?> =
        Observer<ChattingItem?> {
            it?.let { response ->
                messageList.add(
                    ChatMessage(
                        role,
                        response.message.trim(),
                        response.translatedMessage.trim(),
                    )
                )

                chattingAdapter.setMessage(
                    ChatMessage(
                        role,
                        response.message.trim(),
                        response.translatedMessage.trim(),
                    )
                )
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

    // 피드백 챗봇 답장 관찰하기
    private fun getFeedbackResponseObserver(): Observer<FeedbackItem?> =
        Observer {
            it?.let { response ->
                Log.e("response 피드백", response.message)

                // 리스트의 가장 마지막에 있는 유저 값 가져오기
                val message: ChatMessage? = messageList.findLast { it.role == ChatRole.USER }

                message?.feedbackMessage = response.feedbackMessage
                message?.feedbackReason = response.feedbackReason
                message?.translatedFeedbackReason = response.translatedFeedbackReason

                chattingAdapter.setMessage(message)
                scrollToBottom()
            }
        }

    // 힌트 챗봇 답장 관찰하기
    private fun getHintResponseObserver(): Observer<HintItem?> =
        Observer {
            it?.let { response ->
                Log.e("response 힌트", response.hintMessage)

                // 힌트 저장
                hintMessageList.clear() // 리스트 초기화
                hintMessageList.add(response.message)
                hintMessageList.add(response.hintMessage)
                hintMessageList.add(response.translatedHint)

                val newMessage = ChatMessage(
                    role = ChatRole.HINT,
                    message = "힌트",
                    hintMessage = response.hintMessage,
                    translatedHint = response.translatedHint,
                )

                chattingAdapter.setMessage(newMessage)

                scrollToBottom()
            }
        }

    // 메시지 받기
    private fun observeMessage() {
        // 제미나이 메시지 저장
        viewModel.modelChattingResponse.observe(
            viewLifecycleOwner,
            getChattingResponseObserver(ChatRole.MODEL)
        )
        // 사용자 메시지 저장
        viewModel.userChattingResponse.observe(
            viewLifecycleOwner,
            getChattingResponseObserver(ChatRole.USER)
        )
        // 피드백 메시지 저장
        feedbackViewModel.feedbackChattingResponse.observe(
            viewLifecycleOwner,
            getFeedbackResponseObserver()
        )
        // 힌트 메시지 저장
        hintViewModel.hintResponse.observe(
            viewLifecycleOwner,
            getHintResponseObserver()
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
