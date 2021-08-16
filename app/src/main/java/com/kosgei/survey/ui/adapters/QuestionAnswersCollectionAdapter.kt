package com.kosgei.survey.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kosgei.survey.data.model.QuestionAnswerCollection
import com.kosgei.survey.databinding.QuestionAnswerItemBinding


class QuestionAnswersCollectionAdapter(questionAnswerCollections: List<QuestionAnswerCollection>) :
    RecyclerView.Adapter<QuestionAnswersCollectionAdapter.QuestionAnswersCollectionViewHolder>() {

    private val questionAnswerCollections = mutableListOf<QuestionAnswerCollection>()

    init {
        this.questionAnswerCollections.addAll(questionAnswerCollections)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionAnswersCollectionViewHolder {
        return QuestionAnswersCollectionViewHolder(
            QuestionAnswerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: QuestionAnswersCollectionViewHolder, position: Int) {
        holder.setQuestionAnswer(questionAnswerCollections[position])
    }

    class QuestionAnswersCollectionViewHolder(private val questionAnswerItemBinding: QuestionAnswerItemBinding) :
        RecyclerView.ViewHolder(questionAnswerItemBinding.root) {
        fun setQuestionAnswer(questionAnswerCollection: QuestionAnswerCollection) {
           // questionAnswerItemBinding.questionId.text = String.format("id: %s",questionAnswerCollection.uuid )

            val questionAnswersAdapter = QuestionAnswersAdapter(questionAnswers = questionAnswerCollection.answers)
            questionAnswerItemBinding.questionItemRv.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL,false)
            questionAnswerItemBinding.questionItemRv.adapter =questionAnswersAdapter
        }


    }
    class QuestionAnswersDiffUtil(
        private val oldList: List<QuestionAnswerCollection>,
        private val newList: List<QuestionAnswerCollection>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].answers == newList[newItemPosition].answers &&
                    oldList[oldItemPosition].uuid == newList[newItemPosition].uuid
        }

    }

    fun setQuestionAnswerCollection(questionAnswerCollection: List<QuestionAnswerCollection>) {
        val diffCallback = QuestionAnswersDiffUtil(this.questionAnswerCollections, questionAnswerCollection)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.questionAnswerCollections.clear()
        this.questionAnswerCollections.addAll(questionAnswerCollection)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return questionAnswerCollections.size
    }
}