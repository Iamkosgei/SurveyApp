package com.kosgei.survey.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.kosgei.survey.BuildConfig
import com.kosgei.survey.data.dao.SurveyDao
import com.kosgei.survey.data.database.SurveyDb
//import com.kosgei.survey.data.dao.SurveyDao
//import com.kosgei.survey.data.database.SurveyDb
import com.kosgei.survey.data.remote.SurveyApiService
import com.kosgei.survey.utils.BASE_URL
import com.kosgei.survey.utils.ROOM_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSurveyDao(appDatabase: SurveyDb): SurveyDao {
        return appDatabase.surveyDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): SurveyDb {
        return Room.databaseBuilder(
            appContext,
            SurveyDb::class.java,
            ROOM_DB_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideSurveyApiService(retrofit: Retrofit): SurveyApiService =
        retrofit.create(SurveyApiService::class.java)

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = run {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val request = chain.request()
            Timber.i(
                "Sending request to %s",
                request.url
            )

            chain.proceed(request)
        }
        if(BuildConfig.DEBUG){
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(interceptor)
        }


        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        httpClient.build()
    }
}