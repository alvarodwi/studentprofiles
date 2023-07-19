package me.varoa.studentprofiles.feature.favorite

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import logcat.logcat
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.base.UiEvent
import me.varoa.studentprofiles.ext.snackbar
import me.varoa.studentprofiles.feature.favorite.databinding.FragmentFavoriteBinding
import me.varoa.studentprofiles.viewbinding.viewBinding

class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {
    override val binding by viewBinding<FragmentFavoriteBinding>()
    override val viewModel by viewModels<FavoriteViewModel>()

    override fun setupUiEvent() {
        eventJob =
            viewModel.events
                .onEach { event ->
                    when (event) {
                        is UiEvent.Loading -> {
                            toggleLoading(true)
                        }

                        is UiEvent.NotLoading -> {
                            toggleLoading(false)
                        }

                        is UiEvent.Error -> {
                            toggleLoading(false)
                            logcat { "Error : ${event.throwable?.message}" }
                            snackbar("Error : ${event.throwable?.message}")
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun bindView() {
        // help
    }

    override fun toggleLoading(isLoading: Boolean) {
    }
}
