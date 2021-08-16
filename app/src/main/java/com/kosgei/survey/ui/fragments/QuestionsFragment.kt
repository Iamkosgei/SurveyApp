package com.kosgei.survey.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kosgei.survey.R
import com.kosgei.survey.databinding.FragmentQuestionsBinding
import com.kosgei.survey.ui.viewmodels.SurveyViewModel
import com.kosgei.survey.utils.Status
import com.kosgei.survey.utils.isNumber
import com.kosgei.survey.utils.questionTypes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuestionsFragment : Fragment() {

    private var _binding: FragmentQuestionsBinding? = null
    private val binding get() = _binding!!

    private val surveyViewModel: SurveyViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSurvey()

        binding.btnNext.setOnClickListener {
            setNextQuestion()
        }

        isLastQuestion()
    }

    private fun isLastQuestion() {
        surveyViewModel.isLastQuestion.observe(requireActivity(), {
            if (it) {
                binding.btnNext.text = getString(R.string.submit)
            } else {
                binding.btnNext.text = getString(R.string.next)

            }
        })
    }


    private fun getSurvey() {
        binding.btnNext.isGone =true
        surveyViewModel.getSurvey().observe(requireActivity(), {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    binding.btnNext.isGone =false


                    //set question
                    it.data?.let { res ->
                        listenToMessageCount(total = res.questions.size)
                        surveyViewModel.setSurveyQuestions(res)
                    }

                    //get current question after posting
                    getCurrentQuestion()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun listenToMessageCount(total: Int) {
        //initial
        binding.questionCount.text = String.format("%s / %s", 1, total)

        surveyViewModel.answeredQuestionsLiveData.observe(requireActivity(), {
            if (it.size + 1 <= total) {
                binding.questionCount.text = String.format("%s / %s", it.size + 1, total)
            }
        })
    }

    private fun getCurrentQuestion() {
        surveyViewModel.currentQuestion.observe(requireActivity(), {

            binding.question.text = surveyViewModel.getQuestionFromID(it.questionText)

            //be used for validation
            it.answerType

            when (it.questionType) {
                questionTypes.FREE_TEXT.name -> {
                    binding.etInput.isGone = false
                    binding.inputRadioBtn.isGone = true

                }
                questionTypes.SELECT_ONE.name -> {
                    binding.etInput.isGone = true
                    binding.inputRadioBtn.isGone = false

                    binding.inputRadioBtn.orientation = LinearLayout.VERTICAL

                    binding.inputRadioBtn.removeAllViews()

                    for (i in it.options.indices) {
                        val option = it.options[i]
                        val radioButton = RadioButton(requireContext())
                        radioButton.id = View.generateViewId()
                        radioButton.text = option.value
                        binding.inputRadioBtn.addView(radioButton)
                    }

                }
                questionTypes.TYPE_VALUE.name -> {
                    binding.etInput.isGone = false
                    binding.inputRadioBtn.isGone = true
                }

            }

            surveyViewModel.checkIfIsLastQuestion(question = it)
        })
    }

    private fun setNextQuestion() {
        //validation
        val currentQuestion = surveyViewModel.currentQuestion

        when (currentQuestion.value?.questionType) {

            questionTypes.FREE_TEXT.name -> {
                val inputText = binding.etInput.text
                if (inputText.trim().isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.please_enter_a_value), Toast.LENGTH_SHORT)
                        .show()

                } else {
                    surveyViewModel.setQuestion(id = null, answer = inputText.trim().toString())
                    clearInputText()

                }
            }

            questionTypes.TYPE_VALUE.name -> {
                val inputText = binding.etInput.text
                if (inputText.trim().isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.please_enter_a_value), Toast.LENGTH_SHORT)
                        .show()

                } else {

                    if (isNumber(inputText.trim().toString())) {
                        surveyViewModel.setQuestion(id = null, answer = inputText.trim().toString())
                        clearInputText()
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.value_not_valid), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
            questionTypes.SELECT_ONE.name -> {
                if (binding.inputRadioBtn.checkedRadioButtonId == -1) {
                    Toast.makeText(requireContext(), getString(R.string.please_enter_a_value), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val selectedId: Int = binding.inputRadioBtn.checkedRadioButtonId

                    val radioBtn = binding.inputRadioBtn.findViewById<RadioButton>(selectedId)

                   surveyViewModel.setQuestion(id = null, answer =radioBtn.text.toString())
                    binding.inputRadioBtn.removeAllViews()

                }


            }
        }
    }

    private fun clearInputText() {
        binding.etInput.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}