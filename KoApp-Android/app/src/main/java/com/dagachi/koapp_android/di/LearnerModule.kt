package com.dagachi.koapp_android.di

import com.dagachi.koapp_android.data.remote.api.learner.home.LearnerHomeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/*
* 학습자의 모든 화면에서 사용하는 모듈
* Hilt가 Api의 인스턴스를 어떻게 제공해야 하는지 알게하도록 하는 작업을 수행함
* */
@Module
@InstallIn(SingletonComponent::class)
object LearnerModule {
    @Provides
    @Singleton
    fun provideLearnerHomeApi(retrofit: Retrofit): LearnerHomeApi {
        return retrofit.create(LearnerHomeApi::class.java)
    }
}