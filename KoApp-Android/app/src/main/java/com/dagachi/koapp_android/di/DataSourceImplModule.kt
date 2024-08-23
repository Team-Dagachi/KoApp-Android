package com.dagachi.koapp_android.di

import com.dagachi.koapp_android.data.remote.api.learner.home.LearnerHomeApi
import com.dagachi.koapp_android.data.repository.remote.datasource.learner.LearnerHomeDataSource
import com.dagachi.koapp_android.data.repository.remote.datasourceImpl.learner.LearnerHomeDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceImplModule {
    @Provides
    @Singleton
    fun provideLearnerHomeDataSource(
        learnerHomeApi: LearnerHomeApi
    ): LearnerHomeDataSource {
        return LearnerHomeDataSourceImpl(learnerHomeApi)
    }
}