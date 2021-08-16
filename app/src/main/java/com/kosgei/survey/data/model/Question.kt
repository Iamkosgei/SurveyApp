package com.kosgei.survey.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class Question(
    @PrimaryKey
    val id: String,

    @SerializedName("answer_type")
    val answerType: String,

    val next: String?,

    val options: List<Option>,
    @SerializedName("question_text")
    val questionText: String,
    @SerializedName("question_type")
    val questionType: String
)