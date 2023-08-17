package me.varoa.studentprofiles

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import me.varoa.studentprofiles.core.di.CORE_MODULE
import me.varoa.studentprofiles.core.di.LOCAL_MODULE
import me.varoa.studentprofiles.core.di.REMOTE_MODULE
import me.varoa.studentprofiles.core.di.REPOSITORY_MODULE
import me.varoa.studentprofiles.core.di.USE_CASE_MODULE
import me.varoa.studentprofiles.di.APP_MODULE
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
                    LOCAL_MODULE,
                    REMOTE_MODULE,
                    REPOSITORY_MODULE,
                    USE_CASE_MODULE,
                    CORE_MODULE,
                    APP_MODULE,
                ),
            )
        }
    }
}
