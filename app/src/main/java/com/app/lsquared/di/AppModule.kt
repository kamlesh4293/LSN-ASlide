package com.app.lsquared.di

import android.content.Context
import com.app.lsquared.utils.DataParsing
import com.app.lsquared.utils.MySharePrefernce
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharePref(@ApplicationContext context: Context): MySharePrefernce {
        return MySharePrefernce(context)
    }


}