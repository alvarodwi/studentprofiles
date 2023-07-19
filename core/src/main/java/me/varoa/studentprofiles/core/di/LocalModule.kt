package me.varoa.studentprofiles.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.varoa.studentprofiles.core.data.local.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "studentprofiles.db",
        ).build()

    @Provides
    fun provideStudentDao(database: AppDatabase) = database.studentDao

    @Provides
    fun provideFavoriteDao(database: AppDatabase) = database.favoriteDao
}
