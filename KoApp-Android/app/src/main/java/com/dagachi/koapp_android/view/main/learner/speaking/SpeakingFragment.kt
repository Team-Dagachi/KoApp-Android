package com.dagachi.koapp_android.view.main.learner.speaking

import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.databinding.FragmentSpeakingBinding
import com.dagachi.koapp_android.view.base.BaseFragment

/* 학습자의 말하기 연습 화면 */
class SpeakingFragment: BaseFragment<FragmentSpeakingBinding>(FragmentSpeakingBinding::inflate) {
    override fun initViewCreated() {
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Main_10) // status bar 색상
        WindowInsetsControllerCompat(mainActivity!!.window, mainActivity!!.window.decorView).isAppearanceLightStatusBars = true

        mainActivity?.hideLearnerBottomNav(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Gray_100) // status bar 색상
    }

    override fun initAfterBinding() {
        // 클릭 이벤트 모음 함수
        clickEvent()
    }

    // 주제 클릭 이벤트
    private fun clickEvent() {
        // 가족 클릭 이벤트
        binding.cLayoutSpeakingFamily.setOnClickListener {
            val actionToSubjectFrm: NavDirections = SpeakingFragmentDirections.actionSpeakingFrmToSubjectFrm(
                subjectTitle = getString(R.string.speaking_family_translate)
            )
            findNavController().navigate(actionToSubjectFrm)
        }

        // 학교 클릭 이벤트
        binding.cLayoutSpeakingSchool.setOnClickListener {
            val actionToSubjectFrm: NavDirections = SpeakingFragmentDirections.actionSpeakingFrmToSubjectFrm(
                subjectTitle = getString(R.string.speaking_school_translate)
            )
            findNavController().navigate(actionToSubjectFrm)
        }

        // 날씨와 계절 클릭 이벤트
        binding.cLayoutSpeakingWeatherAndSeason.setOnClickListener {
            val actionToSubjectFrm: NavDirections = SpeakingFragmentDirections.actionSpeakingFrmToSubjectFrm(
                subjectTitle = getString(R.string.speaking_weather_and_season_translate)
            )
            findNavController().navigate(actionToSubjectFrm)
        }

        // 여행 클릭 이벤트
        binding.cLayoutSpeakingTravel.setOnClickListener {
            val actionToSubjectFrm: NavDirections = SpeakingFragmentDirections.actionSpeakingFrmToSubjectFrm(
                subjectTitle = getString(R.string.speaking_travel_translate)
            )
            findNavController().navigate(actionToSubjectFrm)
        }

        // 쇼핑 클릭 이벤트
        binding.cLayoutSpeakingShopping.setOnClickListener {
            val actionToSubjectFrm: NavDirections = SpeakingFragmentDirections.actionSpeakingFrmToSubjectFrm(
                subjectTitle = getString(R.string.speaking_shopping_translate)
            )
            findNavController().navigate(actionToSubjectFrm)
        }

        // 미디어와 콘텐츠
        binding.cLayoutSpeakingMediaAndContent.setOnClickListener {
            val actionToSubjectFrm: NavDirections = SpeakingFragmentDirections.actionSpeakingFrmToSubjectFrm(
                subjectTitle = getString(R.string.speaking_media_and_content_translate)
            )
            findNavController().navigate(actionToSubjectFrm)
        }
    }
}