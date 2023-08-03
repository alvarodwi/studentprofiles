package me.varoa.studentprofiles.di

import me.varoa.studentprofiles.screen.detail.DetailViewModel
import me.varoa.studentprofiles.screen.home.HomeViewModel
import me.varoa.studentprofiles.screen.home.filter.FilterSheetViewModel
import me.varoa.studentprofiles.screen.home.sort.SortSheetViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::FilterSheetViewModel)
        viewModelOf(::SortSheetViewModel)
        viewModelOf(::DetailViewModel)
    }
