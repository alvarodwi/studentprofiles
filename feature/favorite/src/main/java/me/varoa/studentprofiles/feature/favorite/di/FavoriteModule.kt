package me.varoa.studentprofiles.feature.favorite.di

import me.varoa.studentprofiles.feature.favorite.screen.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule =
    module {
        viewModel { FavoriteViewModel(get()) }
    }
