package me.varoa.studentprofiles.feature.favorite

import dagger.hilt.android.lifecycle.HiltViewModel
import me.varoa.studentprofiles.base.BaseViewModel
import me.varoa.studentprofiles.core.domain.usecase.FavoriteListUseCase
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
    @Inject
    constructor(
        private val useCase: FavoriteListUseCase,
    ) : BaseViewModel()
