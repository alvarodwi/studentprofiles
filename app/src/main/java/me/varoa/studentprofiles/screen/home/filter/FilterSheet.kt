package me.varoa.studentprofiles.screen.home.filter

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.core.data.local.query.FilterKey
import me.varoa.studentprofiles.core.domain.model.AttackType
import me.varoa.studentprofiles.core.domain.model.DefenseType
import me.varoa.studentprofiles.core.domain.model.School
import me.varoa.studentprofiles.core.domain.model.SquadType
import me.varoa.studentprofiles.core.domain.model.StudentPosition
import me.varoa.studentprofiles.core.domain.model.TacticRole
import me.varoa.studentprofiles.core.domain.model.WeaponType
import me.varoa.studentprofiles.databinding.SheetFilterBinding
import me.varoa.studentprofiles.screen.home.HomeViewModel
import me.varoa.studentprofiles.viewbinding.viewBinding
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterSheet : BottomSheetDialogFragment(R.layout.sheet_filter) {
    private val binding by viewBinding<SheetFilterBinding>()
    private val homeViewModel by koinNavGraphViewModel<HomeViewModel>(R.id.nav_home)
    private val viewModel by viewModel<FilterSheetViewModel>()

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
        setupFilters()
        observeUiState()

        binding.btnReset.setOnClickListener {
            onReset()
        }
    }

    private fun observeQuery() =
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.query.collectLatest { query ->
                viewModel.updateFilter(query.filter)
            }
        }

    private fun observeUiState() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                logcat { "uiState observer -> $state" }
                with(binding) {
                    btnReset.isEnabled = state.canReset
                }
            }
        }

    private fun setupFilters() {
        val currentState = viewModel.uiState.value
        with(binding) {
            // game server
            with(cgIsGlobal) {
                mapOf(
                    "Global" to true,
                    "Japan" to false,
                ).forEach { (t, u) ->
                    this.addView(
                        createChip(u, t, currentState.filterKey.isGlobalServer == u, false) { isInGlobal, _ ->
                            homeViewModel.updateFilter(currentState.filterKey.copy(isGlobalServer = isInGlobal))
                        },
                    )
                }
            }
            // squad type
            with(cgSquadType) {
                enumValues<SquadType>().forEach {
                    this.addView(
                        createChip(it, it.name, currentState.filterKey.squadType == it) { type, checked ->
                            homeViewModel.updateFilter(currentState.filterKey.copy(squadType = if (checked) type else null))
                        },
                    )
                }
            }
            // position
            with(cgPosition) {
                enumValues<StudentPosition>().forEach {
                    this.addView(
                        createChip(it, it.name, currentState.filterKey.position == it) { position, checked ->
                            homeViewModel.updateFilter(currentState.filterKey.copy(position = if (checked) position else null))
                        },
                    )
                }
            }
            // tacticRole
            with(cgRole) {
                enumValues<TacticRole>().forEach {
                    this.addView(
                        createChip(it, it.name, currentState.filterKey.tacticRole == it) { tacticRole, checked ->
                            homeViewModel.updateFilter(currentState.filterKey.copy(tacticRole = if (checked) tacticRole else null))
                        },
                    )
                }
            }
            // atk type
            with(cgAtkType) {
                enumValues<AttackType>().forEach {
                    this.addView(
                        createChip(it, it.name, currentState.filterKey.attackType == it) { attackType, checked ->
                            homeViewModel.updateFilter(currentState.filterKey.copy(attackType = if (checked) attackType else null))
                        },
                    )
                }
            }
            // def type
            with(cgDefType) {
                enumValues<DefenseType>().forEach {
                    this.addView(
                        createChip(it, it.name, currentState.filterKey.defenseType == it) { defenseType, checked ->
                            homeViewModel.updateFilter(currentState.filterKey.copy(defenseType = if (checked) defenseType else null))
                        },
                    )
                }
            }
            // weapon
            with(cgWeaponType) {
                enumValues<WeaponType>().forEach {
                    this.addView(
                        createChip(it, it.name, currentState.filterKey.weaponType == it) { weaponType, checked ->
                            homeViewModel.updateFilter(currentState.filterKey.copy(weaponType = if (checked) weaponType else null))
                        },
                    )
                }
            }
            // school
            with(cgSchool) {
                enumValues<School>().forEach {
                    this.addView(
                        createChip(it, it.key, currentState.filterKey.school == it) { school, checked ->
                            homeViewModel.updateFilter(currentState.filterKey.copy(school = if (checked) school else null))
                        },
                    )
                }
            }
        }
    }

    private fun <T> createChip(
        data: T,
        name: String,
        isChipChecked: Boolean,
        canUncheck: Boolean = true,
        onClick: (T, Boolean) -> Unit,
    ): Chip {
        val chip = Chip(requireContext())
        with(chip) {
            id = ViewCompat.generateViewId()
            text = name
            setChipDrawable(
                ChipDrawable.createFromAttributes(
                    binding.root.context,
                    null,
                    0,
                    com.google.android.material.R.style.Widget_Material3_Chip_Filter,
                ),
            )
            isCheckedIconVisible = false
            isChecked = isChipChecked
            setOnCheckedChangeListener { _, checked ->
                if (canUncheck) {
                    isChecked = checked
                }
                onClick(data, checked)
                viewModel.updateCanReset(true)
            }
        }
        return chip
    }

    private fun onReset() {
        // ui after reset
        with(binding) {
            cgSquadType.clearCheck()
            cgPosition.clearCheck()
            cgRole.clearCheck()
            cgAtkType.clearCheck()
            cgDefType.clearCheck()
            cgWeaponType.clearCheck()
            cgSchool.clearCheck()
        }
        // data on reset
        homeViewModel.updateFilter(FilterKey())
        viewModel.reset()
    }
}
