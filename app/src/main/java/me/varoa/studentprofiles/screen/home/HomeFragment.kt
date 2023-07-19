package me.varoa.studentprofiles.screen.home

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.base.UiEvent
import me.varoa.studentprofiles.core.work.SyncWorker
import me.varoa.studentprofiles.databinding.FragmentHomeBinding
import me.varoa.studentprofiles.ext.snackbar
import me.varoa.studentprofiles.viewbinding.viewBinding

class HomeFragment : BaseFragment(R.layout.fragment_home) {
    override val binding by viewBinding<FragmentHomeBinding>()
    override val viewModel by viewModels<HomeViewModel>()

    private lateinit var adapter: HomeAdapter

    override fun setupUiEvent() {
        eventJob =
            viewModel.events
                .onEach { event ->
                    when (event) {
                        is UiEvent.Error -> {
                            toggleLoading(false)
                            logcat { "Error : ${event.throwable?.message}" }
                            snackbar("Error : ${event.throwable?.message}")
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun bindView() {
        binding.swipeRefresh.isEnabled = false
        setupSyncLayout()
        setupHomeLayout()

        observeStudents()
        observeQuery()
    }

    override fun toggleLoading(isLoading: Boolean) {
        with(binding.swipeRefresh) {
            isRefreshing = isLoading
        }
    }

    private fun setupHomeLayout() {
        with(binding.layoutHome) {
            setupAdapter(recyclerView)
            setupSearchBar()
        }
    }

    private fun setupAdapter(view: RecyclerView) {
        adapter =
            HomeAdapter(imageLoader) { student ->
                logcat { "Student ${student.name} clicked!" }
            }
        view.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                logcat { "Collecting adapter loadStateFlow" }
                if (loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                    toggleSyncLayout(adapter.itemCount < 1)
                }
            }
        }
    }

    private fun setupSearchBar() {
    }

    private fun observeStudents() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.students.collectLatest(adapter::submitData)
        }

    private fun observeQuery() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.query.collectLatest {
                logcat { "Latest query -> $it" }
            }
        }

    private fun setupSyncLayout() {
        with(binding.layoutSync) {
            btnSync.setOnClickListener {
                toggleLoading(true)
                // init work
                val syncRequest =
                    OneTimeWorkRequestBuilder<SyncWorker>()
                        .build()
                val workManager = WorkManager.getInstance(requireContext())
                workManager.enqueue(syncRequest)
                // observe when done
                workManager.getWorkInfoByIdLiveData(syncRequest.id)
                    .observe(viewLifecycleOwner) { workInfo ->
                        if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                            snackbar(getString(R.string.snackbar_sync_completed))
                            toggleLoading(false)
                            toggleSyncLayout(false)
                        }
                    }
            }
        }
    }

    private fun toggleSyncLayout(isShown: Boolean) {
        logcat { "toggleSyncLayout($isShown)" }
        binding.layoutHome.root.isVisible = !isShown
        binding.layoutSync.root.isVisible = isShown
    }
}
