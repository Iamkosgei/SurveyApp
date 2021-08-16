package com.kosgei.survey.di


import com.kosgei.survey.data.repositories.SurveyRepository
import com.kosgei.survey.data.repositories.SurveyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class SurveyRepositoryModule {
    @Binds
    abstract fun bindRepository(impl: SurveyRepositoryImpl): SurveyRepository
}