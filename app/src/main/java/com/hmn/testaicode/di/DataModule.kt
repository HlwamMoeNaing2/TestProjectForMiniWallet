package com.hmn.testaicode.di

import android.content.Context
import com.hmn.testaicode.data.AppStorageProviderRepo
import com.hmn.testaicode.data.AppStorageProviderRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppStorageProvider(@ApplicationContext context: Context):AppStorageProviderRepo{
        return AppStorageProviderRepoImpl(context)
    }
}