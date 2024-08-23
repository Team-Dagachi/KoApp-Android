package com.dagachi.koapp_android.di

import com.dagachi.koapp_android.data.repository.learner.LearnerHomeRepositoryImpl
import com.dagachi.koapp_android.data.repository.remote.datasourceImpl.learner.LearnerHomeDataSourceImpl
import com.dagachi.koapp_android.domain.repository.learner.LearnerHomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideLearnerHomeRepository(
        learnerHomeDataSourceImpl: LearnerHomeDataSourceImpl
    ): LearnerHomeRepository {
        return LearnerHomeRepositoryImpl(learnerHomeDataSourceImpl)
    }
}