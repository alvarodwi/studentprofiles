package me.varoa.studentprofiles.core.di

import android.content.Context
import androidx.room.Room
import me.varoa.studentprofiles.core.data.local.AppDatabase
import me.varoa.studentprofiles.core.data.prefs.DataStoreManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule =
    module {
        fun provideDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "studentprofiles.db",
            ).build()

        single { provideDatabase(androidContext()) }

        single {
            val database = get<AppDatabase>()
            database.studentDao
        }

        single {
            val database = get<AppDatabase>()
            database.favoriteDao
        }

        single {
            DataStoreManager(androidApplication())
        }
    }
