package me.varoa.studentprofiles.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.varoa.studentprofiles.core.data.interactor.FavoriteListInteractor
import me.varoa.studentprofiles.core.data.interactor.StudentDetailInteractor
import me.varoa.studentprofiles.core.data.interactor.StudentListInteractor
import me.varoa.studentprofiles.core.data.interactor.SyncStudentInteractor
import me.varoa.studentprofiles.core.domain.usecase.FavoriteListUseCase
import me.varoa.studentprofiles.core.domain.usecase.StudentDetailUseCase
import me.varoa.studentprofiles.core.domain.usecase.StudentListUseCase
import me.varoa.studentprofiles.core.domain.usecase.SyncStudentUseCase

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun provideStudentListUseCase(interactor: StudentListInteractor): StudentListUseCase

    @Binds
    abstract fun provideSyncStudentUseCase(interactor: SyncStudentInteractor): SyncStudentUseCase

    @Binds
    abstract fun provideStudentDetailUseCase(interactor: StudentDetailInteractor): StudentDetailUseCase

    @Binds
    abstract fun provideFavoriteListUseCase(interactor: FavoriteListInteractor): FavoriteListUseCase
}
