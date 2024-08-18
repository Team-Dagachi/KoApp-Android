package com.dagachi.koapp_android.data.repository.remote.datasourceImpl.learner

import com.dagachi.koapp_android.data.remote.api.learner.home.LearnerHomeApi
import com.dagachi.koapp_android.data.remote.model.learner.home.LearnerHomeResponse
import com.dagachi.koapp_android.data.repository.remote.datasource.learner.LearnerHomeDataSource
import retrofit2.Response
import javax.inject.Inject

/*
* 학습자의 홈 화면에서 사용
* DataSource와 ViewModel의 중간다리 역할
* */
class LearnerHomeDataSourceImpl @Inject constructor(
    private val learnerHomeApi: LearnerHomeApi
) : LearnerHomeDataSource {

    // 홈 화면 조회
    override suspend fun getLearnerHome(): Response<LearnerHomeResponse> {
        return learnerHomeApi.getLearnerHome()
    }
}