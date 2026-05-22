package com.hmn.testaicode.di

import com.hmn.testaicode.data.remote.api.WalletApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MiniWalletApiModule {

    private const val BASE_URL = "http://10.83.125.53:8080/api/v1/"

    @Provides
    @Singleton
    @MiniWalletRetrofit
    fun provideMiniWalletRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideWalletApiService(
        @MiniWalletRetrofit retrofit: Retrofit,
    ): WalletApiService = retrofit.create(WalletApiService::class.java)
}
