package com.dagachi.koapp_android.data.repository.learner

import com.dagachi.koapp_android.data.mapper.LearnerHomeMapper
import com.dagachi.koapp_android.data.repository.remote.datasource.learner.LearnerHomeDataSource
import com.dagachi.koapp_android.domain.model.learner.home.LearnerHome
import com.dagachi.koapp_android.domain.repository.learner.LearnerHomeRepository
import javax.inject.Inject

/*
 * DataSource를 이용하기 위한 리포지토리
 */
class LearnerHomeRepositoryImpl @Inject constructor(
    private val learnerHomeDataSource: LearnerHomeDataSource
): LearnerHomeRepository {
    // 홈 화면 조회
    override suspend fun getLearnerHome(): LearnerHome {
        // mapper를 통해 데이터 변환(Data -> Domain)
        return LearnerHomeMapper.mapperToLearnerHome(learnerHomeDataSource.getLearnerHome().body()!!)
    }
}