package me.varoa.studentprofiles.screen.detail

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.databinding.FragmentDetailBinding
import me.varoa.studentprofiles.viewbinding.viewBinding

class DetailFragment : BaseFragment(R.layout.fragment_detail) {
    override val binding by viewBinding<FragmentDetailBinding>()
    override val viewModel by viewModels<DetailViewModel>()

    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var adapter: DetailStateAdapter

    override fun setupUiEvent() {
    }

    override fun bindView() {
        binding.toolbar.apply {
            setNavigationOnClickListener { findNavController().popBackStack() }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_favorite -> {
                        viewModel.toggleFavorite()
                        true
                    }

                    else -> false
                }
            }
        }
        adapter = DetailStateAdapter(this, args.id)
        binding.viewPager.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFavorite.collectLatest {
                binding.toolbar.menu.getItem(0).icon =
                    ContextCompat.getDrawable(
                        requireContext(),
                        if (it) R.drawable.ic_heart_fill else R.drawable.ic_heart,
                    )
            }
        }
    }

    override fun toggleLoading(isLoading: Boolean) {
        // binding
    }
}
