package com.dagachi.koapp_android.view.main.learner.speaking

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.databinding.FragmentSpeakingSituationBinding
import com.dagachi.koapp_android.view.base.BaseFragment
import com.dagachi.koapp_android.view.main.learner.speaking.adapter.SpeakingSituationAdapter
import com.dagachi.koapp_android.widget.utils.LocaleUtils

/* 학습자의 말하기 연습 상황별 화면 */
class SpeakingSituationFragment: BaseFragment<FragmentSpeakingSituationBinding>(FragmentSpeakingSituationBinding::inflate) {

    private var situationList = ArrayList<SpeakingSituation>() // 대화 tip 리스트
    private var situationKorTitle: String? = null // 주제 상황 타이틀

    override fun initViewCreated() {
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Gray_100) // status bar 색상

        // 리스트 초기화
        situationList.clear()

        // 뷰에 전달받은 데이터 적용
        val args: SpeakingSituationFragmentArgs by navArgs()
        when (args.situationTitle) {
            // 학교(선생님, 친구)
            getString(R.string.speaking_school_teacher) -> {
                binding.tvSpeakingSituation.text = getString(R.string.speaking_school_teacher_situation)
                getSituationList(R.array.speaking_school_teacher_tip)
            }
            getString(R.string.speaking_school_friend) -> {
                binding.tvSpeakingSituation.text = getString(R.string.speaking_school_friend_situation)
                getSituationList(R.array.speaking_school_friend_tip)
            }
            // 미디어와 콘텐츠(k-pop, 드라마, 게임)
            getString(R.string.speaking_media_and_content_kpop) -> {
                binding.tvSpeakingSituation.text = getString(R.string.speaking_media_and_content_kpop_situation)
                getSituationList(R.array.speaking_media_and_content_kpop_tip)
            }
            getString(R.string.speaking_media_and_content_kdrama) -> {
                binding.tvSpeakingSituation.text = getString(R.string.speaking_media_and_content_kdrama_situation)
                getSituationList(R.array.speaking_media_and_content_kdrama_tip)
            }
            getString(R.string.speaking_media_and_content_game) -> {
                binding.tvSpeakingSituation.text = getString(R.string.speaking_media_and_content_game_situation)
                getSituationList(R.array.speaking_media_and_content_game_tip)
            }
        }

        // 툴바 제목
        setToolbarTitle(binding.toolbarSpeakingSituation.tvSubToolbarTitle, args.situationKorTitle)
        situationKorTitle = args.situationKorTitle

        // 툴바 표현집 버튼 보이기
        binding.toolbarSpeakingSituation.ivSubToolbarRight.visibility = View.VISIBLE
    }

    override fun initAfterBinding() {
        // 뷰 초기화
        binding.rvSpeakingSituation.removeAllViews()

        // 어댑터 연결
        val adapter = SpeakingSituationAdapter(situationList)
        binding.rvSpeakingSituation.adapter = adapter

        // 뒤로가기 버튼 클릭 이벤트
        binding.toolbarSpeakingSituation.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 표현집으로 이동하기 버튼 클릭 이벤트
        binding.toolbarSpeakingSituation.ivSubToolbarRight.setOnClickListener {
            // TODO: 표현집 화면으로 이동
        }

        // 연습하기 버튼 클릭 이벤트
        binding.btnSpeakingPractice.setOnClickListener {
            // 말하기 연습 챗봇 화면으로 이동
            val actionToChattingFrm = SpeakingSituationFragmentDirections.actionSituationFrmToChattingFrm(
                situationKorTitle = situationKorTitle!!
            )
            findNavController().navigate(actionToChattingFrm)
        }
    }

    // 앱 언어에 맞는 텍스트 데이터 가져오기
    @SuppressLint("Recycle")
    private fun getSituationList(arrayId: Int) {
        // 앱 언어가 한국어라면
        if (LocaleUtils.getCurrentLanguage(requireContext()) == "ko") {
            for (situation in resources.getStringArray(arrayId)) {
                situationList.add(SpeakingSituation(situation))
            }
        }
        // 앱 언어가 외국어라면
        else {
            val typedArray = resources.obtainTypedArray(arrayId)
            for (i in 0 until typedArray.length()) {
                val resId = typedArray.getResourceId(i, 0)
                if (resId != 0) {
                    val situation = resources.getString(resId)
                    situationList.add(SpeakingSituation(situation, LocaleUtils.getKoreanStrings(requireContext(), resId)))
                }
            }
        }
    }
}