package com.kosgei.survey.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kosgei.survey.data.repositories.SurveyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: SurveyRepository
) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val notUploadedSurvey = repository.getUnploadedAnswers()

        if (notUploadedSurvey.isNotEmpty()) {
            var savedAnswersResponse = repository.sendAnswers(notUploadedSurvey)

            if (savedAnswersResponse) {
                //mark as saved

                var updatedIds = notUploadedSurvey.map { it.id }.toList()


                repository.setAnswersAsUploaded(ids = updatedIds)

            }

        }

        return Result.success()
    }
}
