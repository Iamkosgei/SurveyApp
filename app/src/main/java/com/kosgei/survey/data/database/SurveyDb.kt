package com.kosgei.survey.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kosgei.survey.data.converters.MyTypeConverters
import com.kosgei.survey.data.dao.SurveyDao
import com.kosgei.survey.data.model.*
import com.kosgei.survey.utils.ROOM_VERSION


@Database(
    entities = [QuestionAnswer::class, SurveyResponse::class],
    version = ROOM_VERSION,
    exportSchema = false
)
@TypeConverters(MyTypeConverters::class)
abstract class SurveyDb : RoomDatabase() {
    abstract fun surveyDao(): SurveyDao
}