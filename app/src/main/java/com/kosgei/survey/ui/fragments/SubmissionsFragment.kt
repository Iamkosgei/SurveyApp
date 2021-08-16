package com.kosgei.survey.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kosgei.survey.databinding.FragmentSubmissionsBinding
import com.kosgei.survey.ui.adapters.QuestionAnswersCollectionAdapter
import com.kosgei.survey.ui.viewmodels.SurveyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SubmissionsFragment : Fragment() {

    private var _binding: FragmentSubmissionsBinding? = null
    private val binding get() = _binding!!

    private val surveyViewModel: SurveyViewModel by viewModels()



    private lateinit var recyclerView: RecyclerView

    private lateinit var questionAnswersAdapter: QuestionAnswersCollectionAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSubmissionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.submissionsRv

        //set up recycler view
        setUpRecyclerView()

        getSavedAnswers()
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        questionAnswersAdapter = QuestionAnswersCollectionAdapter(arrayListOf())
        recyclerView.adapter = questionAnswersAdapter
    }

    private fun getSavedAnswers() {
        binding.progressBar.isGone =false
        binding.noSurveyCompleted.isGone =true
        viewLifecycleOwner.lifecycleScope.launch {
            surveyViewModel.getSavedAnswers().observe(requireActivity(), {
                binding.progressBar.isGone =true
               val transformedAnswers = surveyViewModel.transformQuestionAnswers(
                   it
               )
                if (transformedAnswers.isEmpty()){
                    binding.noSurveyCompleted.isGone =false

                }
                else{
                    questionAnswersAdapter.setQuestionAnswerCollection(
                        transformedAnswers
                    )
                }


            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}