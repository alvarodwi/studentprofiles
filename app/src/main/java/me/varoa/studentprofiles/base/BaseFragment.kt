package me.varoa.studentprofiles.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import coil.ImageLoader
import org.koin.android.ext.android.inject

abstract class BaseFragment(
    @LayoutRes layoutId: Int,
) : Fragment(layoutId) {
    protected val imageLoader: ImageLoader by inject()

    protected abstract val binding: ViewBinding
    protected abstract val viewModel: ViewModel

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
    }

    abstract fun bindView()

    protected fun navigateTo(directions: NavDirections) {
        findNavController().navigate(directions)
    }
}
