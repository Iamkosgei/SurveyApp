package com.kosgei.survey.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kosgei.survey.data.model.Question
import com.kosgei.survey.data.model.QuestionAnswer
import com.kosgei.survey.data.model.QuestionAnswerCollection
import com.kosgei.survey.data.model.SurveyResponse
import com.kosgei.survey.data.repositories.SurveyRepository
import com.kosgei.survey.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kpropmap.propMapOf
import timber.log.Timber
import java.lang.Error
import java.util.*
import javax.inject.Inject


@HiltViewModel
class SurveyViewModel @Inject constructor(private val surveyRepository: SurveyRepository) :
    ViewModel() {
    private var _surveyResponse = MutableLiveData<ResultWrapper<SurveyResponse>>()


    fun getSurvey(): MutableLiveData<ResultWrapper<SurveyResponse>>{
        viewModelScope.launch {
            surveyRepository.fetchSurvey().collect {
                _surveyResponse.postValue(it)
            }

        }
        return  _surveyResponse

    }

    //to enable retry
    fun fetchSurvey() {
        viewModelScope.launch {
            surveyRepository.fetchSurvey().collect {
                _surveyResponse.postValue(it)
            }

        }
    }


    //uuid to indentify answers in belonging to a single session
    private lateinit var uuid: String

    //fetched questions
    private lateinit var _questions: SurveyResponse

    //answered questions
    var answeredQuestions = mutableSetOf<QuestionAnswer>()
    var answeredQuestionsLiveData = MutableLiveData<List<QuestionAnswer>>()

    //current question
    var currentQuestion = MutableLiveData<Question>()

    //is last question
    var isLastQuestion = MutableLiveData<Boolean>()


    fun setSurveyQuestions(survey: SurveyResponse) {

        //generate uuid for session
        uuid = UUID.randomUUID().toString()

        _questions = survey

        //set is last to false by default
        isLastQuestion.postValue(false)

        //first time set to the startQuestionId
        setQuestion(_questions.startQuestionId, answer = null)
    }

    fun checkIfIsLastQuestion(question: Question) {
        isLastQuestion.postValue(question.next == null)
    }

    //set current question
    //startQuestionId
    fun setQuestion(id: String?, answer: String?) {
        // maybe background ?
        if (id != null) {
            currentQuestion.postValue(_questions.questions.first { it.id == id })
        } else {

            //save answered question
            if (answer != null) {
                saveAnsweredQuestion(answer = answer)
            }

            val currentId: String? = currentQuestion.value?.next
            if (currentId != null) {
                currentQuestion.postValue(_questions.questions.first { it.id == currentId })
            } else {
                saveAnswerToDb()
                resetSession()
            }
        }
    }

    private fun resetSession() {
        setSurveyQuestions(_questions)

        //clear answers
        answeredQuestions.clear()
        answeredQuestionsLiveData.value = answeredQuestions.toList()
    }

    private fun saveAnswerToDb() {
        viewModelScope.launch {
            surveyRepository.saveAnswer(answeredQuestions.toList())

        }

    }

    private fun saveAnsweredQuestion(answer: String) {
        //add current question to list of answered questions

        val currentQuestion = currentQuestion.value

        if (currentQuestion != null) {
            answeredQuestions.add(
                QuestionAnswer(
                    answer = answer,
                    questionId = currentQuestion.id,
                    question = getQuestionFromID(currentQuestion.id),
                    id = 0,
                    uploaded = false,
                    uuid = uuid
                )
            )

            answeredQuestionsLiveData.value = answeredQuestions.toList()
        }
    }

    fun getQuestionFromID(id: String): String {
        //reflection
        try {
            val name = _questions.strings.en.javaClass
                .getMethod("get" + formatStringForReflection(id = id)) // to get property called `name`
                .invoke(_questions.strings.en)
            if (name is String) {
                return name
            }
            return id

        } catch (e: Error) {
            return id

        }
    }

    private fun formatStringForReflection(id: String): String {
        val idArray = id.split("_")
        if (idArray.size > 1) {
            var formattedStr = ""

            for (i in idArray.indices) {
                formattedStr += idArray[i].capitalize()
            }

            return formattedStr
        }
        return idArray.first()
    }


    //saved answers
    suspend fun getSavedAnswers()= surveyRepository.getSavedAnswers()

    fun transformQuestionAnswers(answers: List<QuestionAnswer>): List<QuestionAnswerCollection> {
        val answersMap = mutableMapOf<String, QuestionAnswerCollection>()

        for (i in answers.indices) {
            val questionAnswer = answers[i]
            val questionList = mutableListOf<QuestionAnswer>()

            if (answersMap.containsKey(questionAnswer.uuid)) {
                answersMap[questionAnswer.uuid]?.answers?.let { questionList.addAll(it) }
                questionList.add(questionAnswer)
                answersMap[questionAnswer.uuid]?.answers = questionList.toList()
            } else {
                questionList.add(questionAnswer)
                answersMap[questionAnswer.uuid] =
                    QuestionAnswerCollection(questionAnswer.uuid, questionList.toList())
            }

        }
        val questionAnswerCollection = mutableListOf<QuestionAnswerCollection>()

        answersMap.forEach { (_, value) -> questionAnswerCollection.add(value) }
        return questionAnswerCollection
    }
}