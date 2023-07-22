package me.varoa.studentprofiles.screen.home.sort

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.databinding.SheetSortBinding
import me.varoa.studentprofiles.screen.home.HomeViewModel
import me.varoa.studentprofiles.viewbinding.viewBinding

class SortSheet : BottomSheetDialogFragment(R.layout.sheet_sort) {
    private val binding by viewBinding<SheetSortBinding>()
    private val homeViewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.nav_home)
    private val viewModel by viewModels<SortSheetViewModel>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().setOnShowListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            bottomSheetBehavior.isHideable = false
            val bottomSheetParent = binding.container
            BottomSheetBehavior.from(bottomSheetParent.parent as View).peekHeight =
                bottomSheetParent.height
            bottomSheetBehavior.peekHeight = bottomSheetParent.height
            bottomSheetParent.parent.requestLayout()
        }

        observeQuery()
        loadChips()
    }

    private fun observeQuery() =
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.query.collectLatest { query ->
                viewModel.updateSort(query.sort)
            }
        }

    private fun loadChips() {
        val currentState = viewModel.uiState.value
        with(binding.cgSort) {
            viewModel.sortKeys.forEach { sortKey ->
                val chip = Chip(this.context)
                with(chip) {
                    id = sortKey.ordinal
                    text = sortKey.name
                    setChipDrawable(
                        ChipDrawable.createFromAttributes(
                            binding.root.context,
                            null,
                            0,
                            com.google.android.material.R.style.Widget_Material3_Chip_Filter,
                        ),
                    )
                    isCheckedIconVisible = false
                    if (currentState.sortKey.name == sortKey.name) {
                        isChecked = true
                    }
                    setOnCheckedChangeListener { _, _ ->
                        homeViewModel.updateSort(sortKey)
                    }
                }
                addView(chip)
            }
        }
    }
}
