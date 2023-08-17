package me.varoa.studentprofiles.feature.favorite.screen

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.feature.favorite.R
import me.varoa.studentprofiles.feature.favorite.R.layout
import me.varoa.studentprofiles.feature.favorite.databinding.FragmentFavoriteBinding
import me.varoa.studentprofiles.feature.favorite.di.favoriteModule
import me.varoa.studentprofiles.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : BaseFragment(layout.fragment_favorite) {
    override val binding by viewBinding<FragmentFavoriteBinding>()
    override val viewModel by viewModel<FavoriteViewModel>()

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().also {
            loadKoinModules(favoriteModule)
        }
    }

    override fun bindView() {
        with(binding.toolbar) {
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
        setupErrorLayout()

        adapter =
            FavoriteAdapter(imageLoader) {
                navigateTo(FavoriteFragmentDirections.actionFavoriteToDetail(it.id))
            }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                if (loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                    toggleErrorLayout(adapter.itemCount < 1)
                }
            }
        }
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.students.collectLatest {
                logcat { "Students data -> $it" }
                adapter.submitData(it)
            }
        }
    }

    private fun setupErrorLayout() {
        with(binding) {
            tvTitle.text = getString(R.string.title_favorite_empty)
            tvBody.text = getString(R.string.info_favorite_empty)
        }
    }

    private fun toggleErrorLayout(isShown: Boolean) {
        with(binding) {
            recyclerView.isVisible = !isShown
            layoutError.isVisible = isShown
        }
    }
}
