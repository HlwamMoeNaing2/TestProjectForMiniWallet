package com.hmn.testaicode.di

import com.hmn.testaicode.ui.screens.utils.BitmapEncoder
import com.hmn.testaicode.ui.screens.utils.BitmapEncoderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {

    @Binds
    @Singleton
    abstract fun bindBitmapEncoder(impl: BitmapEncoderImpl): BitmapEncoder
}
