package me.varoa.studentprofiles.core.di

import me.varoa.studentprofiles.core.data.interactor.FavoriteListInteractor
import me.varoa.studentprofiles.core.data.interactor.StudentDetailInteractor
import me.varoa.studentprofiles.core.data.interactor.StudentListInteractor
import me.varoa.studentprofiles.core.data.interactor.SyncStudentInteractor
import me.varoa.studentprofiles.core.domain.usecase.FavoriteListUseCase
import me.varoa.studentprofiles.core.domain.usecase.StudentDetailUseCase
import me.varoa.studentprofiles.core.domain.usecase.StudentListUseCase
import me.varoa.studentprofiles.core.domain.usecase.SyncStudentUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        single<StudentListUseCase> { StudentListInteractor(get()) }
        single<SyncStudentUseCase> { SyncStudentInteractor(get()) }
        single<StudentDetailUseCase> { StudentDetailInteractor(get(), get()) }
        single<FavoriteListUseCase> { FavoriteListInteractor(get()) }
    }
