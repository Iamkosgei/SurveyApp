package com.kosgei.survey.data.model


import com.google.gson.annotations.SerializedName

data class QuestionStr(
    @SerializedName("opt_female")
    val optFemale: String,
    @SerializedName("opt_male")
    val optMale: String,
    @SerializedName("opt_other")
    val optOther: String,
    @SerializedName("q_farmer_gender")
    val qFarmerGender: String,
    @SerializedName("q_farmer_name")
    val qFarmerName: String,
    @SerializedName("q_size_of_farm")
    val qSizeOfFarm: String
)