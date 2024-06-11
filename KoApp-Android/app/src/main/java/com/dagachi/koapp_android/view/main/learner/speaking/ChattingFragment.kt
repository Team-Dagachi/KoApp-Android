package com.dagachi.koapp_android.view.main.learner.speaking

import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.dagachi.koapp_android.BuildConfig
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.data.remote.model.ChatMessage
import com.dagachi.koapp_android.data.remote.model.ChatRole
import com.dagachi.koapp_android.databinding.FragmentChattingBinding
import com.dagachi.koapp_android.view.base.BaseFragment
import com.dagachi.koapp_android.view.main.learner.speaking.adapter.ChattingAdapter
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.ChattingViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

/* Gemini 챗봇과의 주제별 말하기연습 스피킹 화면 */
class ChattingFragment: BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate) {
    private lateinit var viewModel: ChattingViewModel // 뷰모델
    private var chattingAdapter = ChattingAdapter() // 채팅 어댑터
    private var messageList = mutableListOf<ChatMessage>() // 채팅 리스트
    private var systemInstruction: String? = null // 프롬프트
    private var initMessage: String? = null // 초기 메시지

    override fun initViewCreated() {
        mainActivity!!.hideLearnerBottomNav(true)
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Gray_100) // status bar 색상

        val args: ChattingFragmentArgs by navArgs()
        when (args.situationKorTitle) {
            // 학교(선생님, 친구)
            getString(R.string.speaking_school_teacher) -> {
                systemInstruction = getString(R.string.prompt_school_teacher)
                initMessage = "요새 학교 생활은 어때요?"
            }
            getString(R.string.speaking_school_friend) -> {
                systemInstruction = getString(R.string.prompt_school_friend)
                initMessage = "내일 챙겨와야 하는 준비물이 뭐야?"
            }
            // 미디어와 콘텐츠(k-pop, 드라마, 게임)
            getString(R.string.speaking_media_and_content_kpop) -> {
                systemInstruction = getString(R.string.prompt_media_and_content_kpop)
                initMessage = "혹시 좋아하는 K-POP 가수 있어요?"
            }
            getString(R.string.speaking_media_and_content_kdrama) -> {
                systemInstruction = getString(R.string.prompt_media_and_content_kdrama)
                initMessage = "혹시 좋아하는 한국 드라마 있어요?"
            }
            getString(R.string.speaking_media_and_content_game) -> {
                systemInstruction = getString(R.string.prompt_media_and_content_game)
                initMessage = "혹시 좋아하는 게임 있어요?"
            }
        }

        // 툴바 제목 설정
        setToolbarTitle(binding.toolbarChatting.tvSubToolbarTitle, args.situationKorTitle)
    }

    override fun initAfterBinding() {
        // 뒤로가기 버튼 클릭 이벤트
        binding.toolbarChatting.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        val model = GenerativeModel(
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
            systemInstruction = content { text(systemInstruction!!) },
        )

        viewModel = ViewModelProvider(
            this,
            ChattingViewModel.ChattingViewModelFactory(model)
        )[ChattingViewModel::class.java]

        // 초기 질문 메시지 추가
        messageList.add(ChatMessage(ChatRole.MODEL, initMessage!!))

        binding.rvChatting.adapter = chattingAdapter // 어댑터 연결
        chattingAdapter.setMessage(messageList) // 초기 질문 넣기

        sendUserMessage() // 메시지 전송
        observeMessage() // 메시지 받기
    }

    // 메시지 보내기
    private fun sendUserMessage() {
        binding.btnChattingSend.setOnClickListener {
            val userMessage = binding.edtChatting.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                viewModel.geminiChat(userMessage)
                messageList.add(ChatMessage(ChatRole.USER, userMessage)) // 사용자 메시지 추가
                chattingAdapter.setMessage(messageList)
                scrollToBottom()

                // 메시지 입력창에 입력되었던 텍스트 삭제
                binding.edtChatting.text = null
                mainActivity?.getHideKeyboard(binding.root) // 키보드 숨기기
            }
        }
    }

    // 메시지 받기
    private fun observeMessage() {
        viewModel.chattingResponse.observe(viewLifecycleOwner, Observer { response ->
            response.text?.let { message ->
                messageList.add(ChatMessage(ChatRole.MODEL, message.trim()))
                chattingAdapter.setMessage(messageList)
                scrollToBottom()
            }
        })
    }

    // 채팅 목록 하단으로 스크롤
    private fun scrollToBottom() {
        binding.rvChatting.smoothScrollToPosition(chattingAdapter.itemCount - 1)
    }
}