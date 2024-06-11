package com.dagachi.koapp_android.view.main.learner.speaking

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.databinding.FragmentSpeakingSubjectBinding
import com.dagachi.koapp_android.view.base.BaseFragment
import com.dagachi.koapp_android.view.main.learner.speaking.adapter.SpeakingSubjectAdapter
import com.dagachi.koapp_android.viewmodel.main.learner.speaking.SpeakingSubjectViewModel
import com.dagachi.koapp_android.widget.utils.LocaleUtils

/* 학습자의 말하기 연습 주제별 화면 */
class SpeakingSubjectFragment: BaseFragment<FragmentSpeakingSubjectBinding>(FragmentSpeakingSubjectBinding::inflate) {

    private val viewModel: SpeakingSubjectViewModel by viewModels() // 뷰모델
    private var subjectList = ArrayList<SpeakingSubject>() // 주제 리스트

    @SuppressLint("Recycle")
    override fun initViewCreated() {
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Gray_100) // status bar 색상

        mainActivity?.hideLearnerBottomNav(true) // bnv 숨기기

        // 뷰에 전달받은 데이터 적용
        val args: SpeakingSubjectFragmentArgs by navArgs()
        when (args.subjectTitle) {
            getString(R.string.speaking_family_translate) -> binding.ivSpeakingSubjectIcon.setImageResource(R.drawable.ic_family_47)
            getString(R.string.speaking_school_translate) -> {
                binding.ivSpeakingSubjectIcon.setImageResource(R.drawable.ic_school_47)
                getSubjectList(R.array.speaking_school_subject) // 리스트에 값 넣기
            }
            getString(R.string.speaking_weather_and_season_translate) -> binding.ivSpeakingSubjectIcon.setImageResource(R.drawable.ic_weather_52)
            getString(R.string.speaking_travel_translate) -> binding.ivSpeakingSubjectIcon.setImageResource(R.drawable.ic_travel_32)
            getString(R.string.speaking_shopping_translate) -> binding.ivSpeakingSubjectIcon.setImageResource(R.drawable.ic_shopping_45)
            getString(R.string.speaking_media_and_content_translate) -> {
                binding.ivSpeakingSubjectIcon.setImageResource(R.drawable.ic_media_52)
                getSubjectList(R.array.speaking_media_and_content_subject) // 리스트에 값 넣기
            }
        }
        binding.tvSpeakingSubjectTitle.text = args.subjectTitle
    }

    override fun initAfterBinding() {
        // 뒤로가기 버튼 클릭
        binding.ivSpeakingSubjectBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 어댑터 설정
        val subjectAdapter = SpeakingSubjectAdapter(subjectList)
        // 어댑터 연결
        binding.rvSpeakingSubject.adapter = subjectAdapter

        // 주제 박스 클릭 이벤트
        subjectAdapter.onSubjectClickListener = {
            // TODO: 주제별 화면으로 이동
        }
    }

    // 앱 언어에 맞는 텍스트 데이터 가져오기
    @SuppressLint("Recycle")
    private fun getSubjectList(arrayId: Int) {
        // 앱 언어가 한국어라면
        if (LocaleUtils.getCurrentLanguage(requireContext()) == "ko") {
            for (subject in resources.getStringArray(arrayId)) {
                subjectList.add(SpeakingSubject(false, subject.toString()))
            }
        }
        // 앱 언어가 외국어라면
        else {
            val typedArray = resources.obtainTypedArray(arrayId)
            for (i in 0 until typedArray.length()) {
                val resId = typedArray.getResourceId(i, 0)
                if (resId != 0) {
                    val subject = resources.getString(resId)
                    subjectList.add(SpeakingSubject(false, subject, LocaleUtils.getKoreanStrings(requireContext(), resId)))
                }
            }
        }
    }
}