package com.dagachi.koapp_android.domain.usecase.learner

import com.dagachi.koapp_android.domain.model.learner.home.LearnerHome
import com.dagachi.koapp_android.domain.repository.learner.LearnerHomeRepository
import javax.inject.Inject

/* 학습자 홈 화면의 정보를 가져오는 유스케이스 */
class GetLearnerHomeUseCase @Inject constructor(
    private val learnerHomeRepository: LearnerHomeRepository
) {
    suspend operator fun invoke(): LearnerHome {
        return learnerHomeRepository.getLearnerHome()
    }
}