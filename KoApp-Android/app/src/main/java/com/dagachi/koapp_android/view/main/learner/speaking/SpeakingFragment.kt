package com.dagachi.koapp_android.view.main.learner.speaking

import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
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
    }
}