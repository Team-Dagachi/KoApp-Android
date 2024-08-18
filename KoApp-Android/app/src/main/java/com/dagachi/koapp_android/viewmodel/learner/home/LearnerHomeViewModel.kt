package com.dagachi.koapp_android.viewmodel.learner.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dagachi.koapp_android.domain.model.learner.home.AttendanceItem
import com.dagachi.koapp_android.domain.model.learner.home.LearnerHome
import com.dagachi.koapp_android.domain.usecase.learner.GetLearnerHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/* 학습자의 홈 화면에서 사용되는 뷰 모델
* 홈 화면에서 사용되는 UseCase를 모두 파라미터로 받기
* */
@HiltViewModel
class LearnerHomeViewModel @Inject constructor(
    private val learnerHomeUseCase: GetLearnerHomeUseCase
): ViewModel() {
    private var _learnerHomeData = MutableLiveData<LearnerHome>()
    val learnerHomeData: LiveData<LearnerHome> get() = _learnerHomeData

    // 사용자 이름
    private var _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    // 홈 화면 조회
    fun getLearnerHome() = viewModelScope.launch {
        try {
            val response = learnerHomeUseCase.invoke()
            _learnerHomeData.value = response
            _userName.value = "${response.userName}님,"
        } catch (e: Exception) {
            Log.e("LearnerHomeViewModel", e.stackTraceToString())
        }
    }
}