package com.heartlandnewsfeed.app.di

import android.content.Context
import com.heartlandnewsfeed.app.data.remote.HeartlandApiConstants
import com.heartlandnewsfeed.app.data.remote.HeartlandNewsService
import com.heartlandnewsfeed.app.data.remote.HeartlandRadioService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeartlandNetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(HeartlandApiConstants.API_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HeartlandApiConstants.API_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HeartlandApiConstants.API_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        
        return Retrofit.Builder()
            .baseUrl(HeartlandApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideHeartlandNewsService(retrofit: Retrofit): HeartlandNewsService {
        return retrofit.create(HeartlandNewsService::class.java)
    }

    @Provides
    @Singleton
    fun provideHeartlandRadioService(retrofit: Retrofit): HeartlandRadioService {
        return retrofit.create(HeartlandRadioService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object HeartlandDataModule {

    @Provides
    @Singleton
    fun provideNewsRepository(newsService: HeartlandNewsService): com.heartlandnewsfeed.app.data.repository.NewsRepository {
        return com.heartlandnewsfeed.app.data.repository.NewsRepository(newsService)
    }

    @Provides
    @Singleton
    fun provideRadioRepository(radioService: HeartlandRadioService): com.heartlandnewsfeed.app.data.repository.RadioRepository {
        return com.heartlandnewsfeed.app.data.repository.RadioRepository(radioService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object HeartlandContextModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}
