package com.app.lsquared.di

import android.content.Context
import com.app.lsquared.network.VimeoClientAPI
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.DataParsing
import com.app.lsquared.utils.MySharePrefernce
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharePref(@ApplicationContext context: Context): MySharePrefernce {
        return MySharePrefernce(context)
    }

    @Provides
    @Singleton
    fun provideDataParsing(prefernce:MySharePrefernce) : DataParsing{
        return DataParsing(prefernce)
    }

    @Provides
    @Singleton
    fun provideVimeoClientAPI(): Retrofit {
        return Retrofit.Builder()
//            .baseUrl("https://player.vimeo.com/video/")
            .baseUrl(Constant.BASE_URL_VIMEO)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .build()
    }

}