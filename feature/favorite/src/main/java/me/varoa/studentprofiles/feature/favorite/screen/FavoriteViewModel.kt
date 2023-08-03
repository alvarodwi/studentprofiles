package me.varoa.studentprofiles.feature.favorite.screen

import androidx.lifecycle.ViewModel
import me.varoa.studentprofiles.core.domain.usecase.FavoriteListUseCase

class FavoriteViewModel(
    useCase: FavoriteListUseCase,
) : ViewModel() {
    val students = useCase.getFavorites()
}
