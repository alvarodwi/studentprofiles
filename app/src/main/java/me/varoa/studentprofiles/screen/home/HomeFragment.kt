package me.varoa.studentprofiles.screen.home

import android.graphics.Color
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.core.data.local.query.SortDirectionKey
import me.varoa.studentprofiles.core.work.SyncWorker
import me.varoa.studentprofiles.databinding.FragmentHomeBinding
import me.varoa.studentprofiles.ext.snackbar
import me.varoa.studentprofiles.viewbinding.viewBinding

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    override val binding by viewBinding<FragmentHomeBinding>()
    override val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.nav_home)

    private lateinit var adapter: HomeAdapter
    private lateinit var searchView: SearchView

    override fun setupUiEvent() {
    }

    override fun bindView() {
        binding.swipeRefresh.isEnabled = false
        setupBottomBar()
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
            btnSort.setOnClickListener {
                navigateTo(HomeFragmentDirections.actionHomeToSortSheet())
            }
            btnSortDirection.setOnClickListener {
                viewModel.updateSortDirection()
            }
        }
    }

    private fun setupAdapter(view: RecyclerView) {
        adapter =
            HomeAdapter(imageLoader) { student ->
                navigateTo(HomeFragmentDirections.actionHomeToDetail(student.id))
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
        with(binding.layoutHome.toolbar) {
            // search
            val searchAction = menu.findItem(R.id.action_search)
            searchView = searchAction.actionView as SearchView
            searchView.queryHint = getString(R.string.hint_search_view)
            searchView.setBackgroundColor(Color.TRANSPARENT)
            searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText.isNullOrEmpty()) {
                            viewModel.search("")
                        } else {
                            viewModel.search(newText)
                        }
                        return false
                    }
                },
            )
            searchView.setOnSearchClickListener {
                menu.findItem(R.id.action_filter).isVisible = false
            }
            searchView.setOnCloseListener {
                menu.findItem(R.id.action_filter).isVisible = true
                false
            }
            setOnClickListener {
                menu.findItem(R.id.action_search).expandActionView()
            }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_filter -> {
                        navigateTo(HomeFragmentDirections.actionHomeToFilterSheet())
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setupBottomBar() {
        with(binding.bottomAppBar) {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_favorite -> {
                        navigateTo(HomeFragmentDirections.actionHomeToFavorite())
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun observeStudents() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.students.collectLatest(adapter::submitData)
        }

    private fun observeQuery() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.query.collectLatest { query ->
                with(binding.layoutHome) {
                    btnSort.text = query.sort.name
                    btnSortDirection.setIconResource(
                        when (query.sortDirection) {
                            SortDirectionKey.Ascending -> R.drawable.ic_sort_asc
                            else -> R.drawable.ic_sort_desc
                        },
                    )
                }
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
