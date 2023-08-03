package me.varoa.studentprofiles.feature.favorite.screen

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.studentprofiles.base.BaseFragment
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

        adapter =
            FavoriteAdapter(imageLoader) {
                navigateTo(FavoriteFragmentDirections.actionFavoriteToDetail(it.id))
            }
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.students.collectLatest {
                logcat { "Students data -> $it" }
                adapter.submitData(it)
            }
        }
    }
}
