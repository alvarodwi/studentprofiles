package me.varoa.studentprofiles.core.di

import android.content.Context
import androidx.room.Room
import me.varoa.studentprofiles.core.BuildConfig
import me.varoa.studentprofiles.core.data.local.AppDatabase
import me.varoa.studentprofiles.core.data.prefs.DataStoreManager
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val LOCAL_MODULE =
    module {
        fun provideDatabase(context: Context): AppDatabase {
            val passphrase: ByteArray = SQLiteDatabase.getBytes(BuildConfig.DB_PASSPHRASE.toCharArray())
            val factory = SupportFactory(passphrase)
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "studentprofiles.db",
            )
                .openHelperFactory(factory)
                .build()
        }

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
