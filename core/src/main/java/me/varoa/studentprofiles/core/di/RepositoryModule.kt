package me.varoa.studentprofiles.core.di

import me.varoa.studentprofiles.core.data.FavoriteRepositoryImpl
import me.varoa.studentprofiles.core.data.StudentRepositoryImpl
import me.varoa.studentprofiles.core.domain.repository.FavoriteRepository
import me.varoa.studentprofiles.core.domain.repository.StudentRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<StudentRepository> { StudentRepositoryImpl(get()) }
        single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
    }
