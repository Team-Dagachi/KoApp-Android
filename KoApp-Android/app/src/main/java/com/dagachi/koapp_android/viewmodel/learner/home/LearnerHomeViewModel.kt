package com.dagachi.koapp_android.viewmodel.learner.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dagachi.koapp_android.remote.model.LearnerHomeModel

/* 학습자의 홈 화면에서 사용되는 뷰 모델 */
class LearnerHomeViewModel: ViewModel() {
    private val _learnerHomeResponse = MutableLiveData<LearnerHomeModel>()
    val learnerHomeResponse: LiveData<LearnerHomeModel> get() = _learnerHomeResponse

    fun getData() {
        //_data.value = homeRepository.getData()
    }
}