package com.kosgei.survey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kosgei.survey.data.model.QuestionAnswer
import com.kosgei.survey.data.model.SurveyResponse


@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(questionAnswer: List<QuestionAnswer>)

    @Query("SELECT * FROM questionanswer")
    fun getSavedAnswers(): LiveData<List<QuestionAnswer>>

    @Query("SELECT * FROM questionanswer WHERE uploaded = 0")
    suspend fun getUnploadedAnswers(): List<QuestionAnswer>

    @Query("UPDATE questionanswer SET uploaded = 1 WHERE id IN (:ids)")
    fun setAnswersAsUploaded(ids: List<Int>)

    @Query("SELECT * FROM survey_response")
    suspend fun getCachedSurveyResponse(): List<SurveyResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheSurvey(surveyResponse: SurveyResponse)

}