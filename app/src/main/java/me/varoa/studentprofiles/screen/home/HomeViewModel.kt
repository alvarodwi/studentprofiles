package me.varoa.studentprofiles.screen.home

import dagger.hilt.android.lifecycle.HiltViewModel
import me.varoa.studentprofiles.base.BaseViewModel
import me.varoa.studentprofiles.core.domain.usecase.StudentListUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: StudentListUseCase
) : BaseViewModel() {

}
