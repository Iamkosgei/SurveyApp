package com.kosgei.survey.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "survey_response")
data class SurveyResponse(
    @PrimaryKey
    val id: String,

    val questions: List<Question>,

    @SerializedName("start_question_id")
    val startQuestionId: String,

    @SerializedName("strings")
    val strings: En
)

