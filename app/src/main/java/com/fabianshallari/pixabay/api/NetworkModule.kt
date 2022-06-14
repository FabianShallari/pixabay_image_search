package com.fabianshallari.pixabay.api

import com.fabianshallari.pixabay.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    @Singleton
    fun pixabayApi(okHttpClient: OkHttpClient): PixabayApi = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl("https://pixabay.com/api/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()

    @Provides
    @Singleton
    fun pixabayApiClient(api: PixabayApi): PixabayApiClient =
        PixabayApiClient(api, BuildConfig.PIXABAY_API_KEY)
}