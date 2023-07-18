package me.varoa.studentprofiles.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    @Inject protected lateinit var imageLoader: ImageLoader
    protected lateinit var eventJob: Job

    protected abstract val binding: ViewBinding
    protected abstract val viewModel: ViewModel

    protected abstract fun setupUiEvent()

    override fun onStart() {
        super.onStart()
        setupUiEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
    }

    abstract fun bindView()

    override fun onStop() {
        super.onStop()
        if (this::eventJob.isInitialized) {
            eventJob.cancel()
        }
    }

    protected abstract fun toggleLoading(isLoading: Boolean)

    protected fun navigateTo(directions: NavDirections) {
        findNavController().navigate(directions)
    }

    protected fun navigateTo(directions: NavDirections, extras: Navigator.Extras) {
        findNavController().navigate(directions, extras)
    }
}
