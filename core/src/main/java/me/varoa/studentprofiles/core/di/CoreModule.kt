package me.varoa.studentprofiles.core.di

import me.varoa.studentprofiles.core.work.SyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val coreModule =
    module {
        worker {
            SyncWorker(appContext = androidContext(), params = get(), useCase = get(), client = get())
        }
    }
