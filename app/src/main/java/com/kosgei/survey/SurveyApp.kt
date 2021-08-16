package com.kosgei.survey

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.kosgei.survey.worker.UploadWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class SurveyApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()

        //logs
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        //work manager constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //work manager
        val uploadWork = PeriodicWorkRequestBuilder<UploadWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "UploadWork",
            ExistingPeriodicWorkPolicy.KEEP, uploadWork
        )


    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}