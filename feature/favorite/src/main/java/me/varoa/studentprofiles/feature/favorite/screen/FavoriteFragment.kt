package me.varoa.studentprofiles.feature.favorite.screen

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.feature.favorite.R
import me.varoa.studentprofiles.feature.favorite.R.layout
import me.varoa.studentprofiles.feature.favorite.databinding.FragmentFavoriteBinding
import me.varoa.studentprofiles.feature.favorite.di.FAVORITE_MODULE
import me.varoa.studentprofiles.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : BaseFragment(layout.fragment_favorite) {
    override val binding by viewBinding<FragmentFavoriteBinding>()
    override val viewModel by viewModel<FavoriteViewModel>()

    private var adapter: FavoriteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().also {
            loadKoinModules(FAVORITE_MODULE)
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
            adapter?.let {
                it.loadStateFlow.collectLatest { loadState ->
                    if (loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                        toggleErrorLayout(it.itemCount < 1)
                    }
                }
            }
        }
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            adapter?.let {
                viewModel.students.collectLatest(it::submitData)
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

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}
