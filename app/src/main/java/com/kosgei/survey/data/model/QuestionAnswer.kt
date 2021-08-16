package com.kosgei.survey.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionAnswer(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val question:String,
    val questionId: String,
    val answer: String,
    val uploaded:Boolean,
    val uuid:String
)


data class QuestionAnswerCollection(
    val uuid: String,
    var answers:List<QuestionAnswer>
)
