package com.dagachi.koapp_android.di

import com.dagachi.koapp_android.domain.repository.learner.LearnerHomeRepository
import com.dagachi.koapp_android.domain.usecase.learner.GetLearnerHomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideLearnerHomeUseCase(learnerHomeRepository: LearnerHomeRepository) = GetLearnerHomeUseCase(learnerHomeRepository)
}