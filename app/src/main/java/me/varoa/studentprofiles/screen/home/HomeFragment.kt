package me.varoa.studentprofiles.screen.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import logcat.logcat
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.base.UiEvent
import me.varoa.studentprofiles.core.data.local.query.FilterKey
import me.varoa.studentprofiles.core.data.local.query.StudentQuery
import me.varoa.studentprofiles.core.domain.model.AttackType.Mystic
import me.varoa.studentprofiles.core.domain.model.SquadType.Special
import me.varoa.studentprofiles.databinding.FragmentHomeBinding
import me.varoa.studentprofiles.ext.snackbar
import me.varoa.studentprofiles.utils.WorkUtil
import me.varoa.studentprofiles.viewbinding.viewBinding

class HomeFragment : BaseFragment(R.layout.fragment_home) {
    override val binding by viewBinding<FragmentHomeBinding>()
    override val viewModel by viewModels<HomeViewModel>()

    private val query: StudentQuery = StudentQuery()

    override fun setupUiEvent() {
        eventJob = viewModel.events
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
        binding.btnSync.setOnClickListener {
            WorkUtil.startSyncWork(requireContext())
        }

        binding.btnA.setOnClickListener {
            query.filter = FilterKey(squadType = Special, attackType = Mystic)
            query.generateQuery()
        }
    }

    override fun toggleLoading(isLoading: Boolean) {
    }
}
