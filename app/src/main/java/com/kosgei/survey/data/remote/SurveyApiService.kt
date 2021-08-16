package com.kosgei.survey.data.remote

import com.kosgei.survey.data.model.QuestionAnswer
import com.kosgei.survey.data.model.SavedAnswersResponse
import com.kosgei.survey.data.model.SurveyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SurveyApiService {
    @GET("d628facc-ec18-431d-a8fc-9c096e00709a")
    suspend fun getSurvey(): Response<SurveyResponse>

    @POST("https://kosgei.free.beeceptor.com")
    suspend fun sendAnswers(@Body answers:List<QuestionAnswer>): Response<SavedAnswersResponse>
}