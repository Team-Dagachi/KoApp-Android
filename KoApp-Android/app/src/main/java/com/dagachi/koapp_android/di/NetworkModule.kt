package com.dagachi.koapp_android.di

import com.dagachi.koapp_android.BuildConfig
import com.dagachi.koapp_android.config.XAcessTokenIntercepter
import com.dagachi.koapp_android.di.ApplicationClass.Companion.BASE_URL
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    // http 통신 타이머 설정
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .addNetworkInterceptor(XAcessTokenIntercepter()) // JWT 자동 헤더 전송
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            //json 변화기 Factory
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .setLenient()
            // LocalDate 기본 형식 지정
            .registerTypeAdapter(LocalDate::class.java, JsonDeserializer<LocalDate> { json, typeOfT, context ->
                if (json.asJsonPrimitive.isNumber || json.asJsonArray.isJsonObject) {
                    LocalDate.parse("yyyy-MM-dd")
                }
                else
                    null
            })
            // LocalTime 기본 형식 지정
            .registerTypeAdapter(LocalTime::class.java, JsonDeserializer<LocalTime> { json, typeOfT, context ->
                if (json.asJsonPrimitive.isNumber || json.asJsonArray.isJsonObject) {
                    LocalTime.parse("HH:mm")
                }
                else
                    null
            })
            .create()

        return GsonConverterFactory.create(gson)
    }

    // 로그 설정
    private fun getLoggingInterceptor(): HttpLoggingInterceptor  {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        } else {
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE }
        }
    }
}