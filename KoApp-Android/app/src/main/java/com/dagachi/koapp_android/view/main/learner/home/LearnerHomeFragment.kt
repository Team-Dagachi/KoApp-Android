package com.dagachi.koapp_android.view.main.learner.home

import android.content.Context
import androidx.activity.OnBackPressedCallback
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.databinding.FragmentLearnerHomeBinding
import com.dagachi.koapp_android.view.base.BaseFragment

/* 학습자의 홈 화면 */
class LearnerHomeFragment: BaseFragment<FragmentLearnerHomeBinding>(FragmentLearnerHomeBinding::inflate) {
    private lateinit var callback: OnBackPressedCallback // 뒤로가기 콜백

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 핸드폰 뒤로가기 이벤트
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity!!.getBackPressedEvent()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove() // 콜백 제거
    }

    override fun initViewCreated() {
        // 바텀 네비게이션 띄우기
        mainActivity?.hideLearnerBottomNav(false)
    }

    override fun initAfterBinding() {
    }
}