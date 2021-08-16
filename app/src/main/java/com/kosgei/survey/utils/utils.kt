package com.kosgei.survey.utils


// database
const val ROOM_VERSION:Int = 1
const val ROOM_DB_NAME:String = "SurveyDatabase"

// url
const val BASE_URL:  String = "https://run.mocky.io/v3/"


//question types
enum class questionTypes {
    FREE_TEXT,
    SELECT_ONE,
    TYPE_VALUE
}


//answer types
enum class answerTypes {
    SINGLE_LINE_TEXT,
    FLOAT
}

//validate input is number
val integerChars = '0'..'9'

fun isNumber(input: String): Boolean {
    var dotOccurred = 0
    return input.all { it in integerChars || it == '.' && dotOccurred++ < 1 }
}