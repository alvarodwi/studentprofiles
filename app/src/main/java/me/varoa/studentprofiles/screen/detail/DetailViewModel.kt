package me.varoa.studentprofiles.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.domain.usecase.StudentDetailUseCase

class DetailViewModel(
    handle: SavedStateHandle,
    private val useCase: StudentDetailUseCase,
) : ViewModel() {
    private val id = handle.get<Int>(DetailStateAdapter.ARGS_ID) ?: 0

    val student: Flow<Student> = useCase.getStudent(id)

    val isFavorite get() = useCase.getStudent(id).mapLatest { it.isFavorite }

    fun toggleFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isFavorite.first()) {
                    useCase.removeFromFavorite(id)
                } else {
                    useCase.addToFavorite(id)
                }
            }
        }
    }
}
