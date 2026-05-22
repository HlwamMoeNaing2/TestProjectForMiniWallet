package com.hmn.testaicode.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hmn.testaicode.data.AppStorageProviderRepo
import com.hmn.testaicode.data.AppStorageProviderRepoImpl
import com.hmn.testaicode.data.AuthSessionRepository
import com.hmn.testaicode.data.AuthSessionRepositoryImpl
import com.hmn.testaicode.data.UserRepository
import com.hmn.testaicode.data.UserRepositoryImpl
import com.hmn.testaicode.data.WalletUserRepo
import com.hmn.testaicode.data.WalletUserRepoImpl
import com.hmn.testaicode.data.local.authSessionDataStore
import com.hmn.testaicode.di.DataModule.provideWalletUserRepo
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
    fun provideAuthSessionDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.authSessionDataStore

    @Provides
    @Singleton
    fun provideAuthSessionRepository(
        dataStore: DataStore<Preferences>,
    ): AuthSessionRepository{
        return  AuthSessionRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideAppStorageProvider(@ApplicationContext context: Context): AppStorageProviderRepo {
        return AppStorageProviderRepoImpl(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    @Singleton
    fun provideWalletUserRepo(impl: WalletUserRepoImpl): WalletUserRepo = impl
}