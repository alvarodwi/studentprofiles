package me.varoa.studentprofiles.screen.detail

import dagger.hilt.android.lifecycle.HiltViewModel
import me.varoa.studentprofiles.base.BaseViewModel
import me.varoa.studentprofiles.core.domain.usecase.StudentDetailUseCase
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
    @Inject
    constructor(
        private val useCase: StudentDetailUseCase,
    ) : BaseViewModel()
