package com.kosgei.survey.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.kosgei.survey.data.model.En
import com.kosgei.survey.data.model.Option
import com.kosgei.survey.data.model.Question
import com.kosgei.survey.data.model.QuestionStr


class MyTypeConverters {

    @TypeConverter
    fun questionToJson(value: List<Question>) = Gson().toJson(value)

    @TypeConverter
    fun questionJsonToList(value: String) = Gson().fromJson(value, Array<Question>::class.java).toList()

    @TypeConverter
    fun optionsToJson(value: List<Option>) = Gson().toJson(value)

    @TypeConverter
    fun optionsJsonToList(value: String) = Gson().fromJson(value, Array<Option>::class.java).toList()


    //En
    @TypeConverter
    fun fromEn(value: En) = Gson().toJson(value)

    @TypeConverter
    fun toEn(value: String) = Gson().fromJson(value, En::class.java)


    //QuestionStr
    @TypeConverter
    fun fromQuestionStr(value: QuestionStr) = Gson().toJson(value)
    @TypeConverter
    fun toQuestionStr(value: String) = Gson().fromJson(value, QuestionStr::class.java)




}