package me.varoa.studentprofiles

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import me.varoa.studentprofiles.core.di.coreModule
import me.varoa.studentprofiles.core.di.localModule
import me.varoa.studentprofiles.core.di.remoteModule
import me.varoa.studentprofiles.core.di.repositoryModule
import me.varoa.studentprofiles.core.di.useCaseModule
import me.varoa.studentprofiles.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)

        startKoin {
            androidLogger()
            androidContext(this@App)
            workManagerFactory()
            // modules
            modules(
                listOf(
                    localModule,
                    remoteModule,
                    repositoryModule,
                    useCaseModule,
                    coreModule,
                    appModule,
                ),
            )
        }
    }
}
