package me.varoa.studentprofiles.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.varoa.studentprofiles.core.data.FavoriteRepositoryImpl
import me.varoa.studentprofiles.core.data.StudentRepositoryImpl
import me.varoa.studentprofiles.core.domain.repository.FavoriteRepository
import me.varoa.studentprofiles.core.domain.repository.StudentRepository

@Module(includes = [LocalModule::class, RemoteModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideStudentRepository(repositoryImpl: StudentRepositoryImpl): StudentRepository

    @Binds
    abstract fun provideFavoriteRepository(repositoryImpl: FavoriteRepositoryImpl): FavoriteRepository
}
