package me.varoa.studentprofiles.screen.home

import android.graphics.Color
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.core.data.local.query.SortDirectionKey
import me.varoa.studentprofiles.databinding.FragmentHomeBinding
import me.varoa.studentprofiles.viewbinding.viewBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {
    override val binding by viewBinding<FragmentHomeBinding>()
    override val viewModel by koinNavGraphViewModel<HomeViewModel>(R.id.nav_home)

    private lateinit var adapter: HomeAdapter
    private lateinit var searchView: SearchView

    override fun bindView() {
        setupBottomBar()
        setupErrorLayout()
        setupHomeLayout()

        observeStudents()
        observeQuery()
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
                if (loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                    toggleErrorLayout(adapter.itemCount < 1)
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
            searchAction.setOnActionExpandListener(
                object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                        menu.findItem(R.id.action_filter).isVisible = false
                        return true
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                        menu.findItem(R.id.action_filter).isVisible = true
                        viewModel.search("")
                        toggleErrorLayout(false)
                        return true
                    }
                },
            )
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
                    R.id.action_settings -> {
                        navigateTo(HomeFragmentDirections.actionHomeToSettings())
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

    private fun setupErrorLayout() {
        with(binding.layoutHome.layoutError) {
            tvTitle.text = getString(R.string.title_list_empty)
            tvBody.text = getString(R.string.body_list_empty)
        }
    }

    private fun toggleErrorLayout(isShown: Boolean) {
        logcat { "toggleErrorLayout($isShown)" }
        with(binding.layoutHome) {
            layoutList.isVisible = !isShown
            layoutError.root.isVisible = isShown
        }
    }
}
