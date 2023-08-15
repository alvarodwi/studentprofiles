package me.varoa.studentprofiles.screen.sync

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.core.data.prefs.DataStoreManager
import me.varoa.studentprofiles.core.work.SyncWorker
import me.varoa.studentprofiles.databinding.FragmentSyncBinding
import me.varoa.studentprofiles.ext.toast
import me.varoa.studentprofiles.viewbinding.viewBinding
import org.koin.android.ext.android.inject

class SyncFragment : Fragment(R.layout.fragment_sync) {
    private val binding by viewBinding<FragmentSyncBinding>()
    private val prefs by inject<DataStoreManager>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSync.setOnClickListener {
                // update ui
                btnSync.isVisible = false
                tvProgress.isVisible = true
                progressBar.isVisible = true
                tvTitle.text = getString(R.string.title_sync_progress)
                tvSubtitle.text = getString(R.string.subtitle_sync_progress)
                // start sync
                startSync()
            }
        }
    }

    private fun startSync() {
        // init work
        val syncRequest =
            OneTimeWorkRequestBuilder<SyncWorker>()
                .build()
        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueue(syncRequest)
        // observe when done
        workManager.getWorkInfoByIdLiveData(syncRequest.id)
            .observe(viewLifecycleOwner) { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        val message = workInfo.outputData.getString(SyncWorker.KEY_MESSAGE)
                        message?.let { toast(it) }
                        viewLifecycleOwner.lifecycleScope.launch {
                            prefs.finishFirstTimeSync()
                            findNavController().navigate(
                                SyncFragmentDirections.actionSyncToHome(),
                            )
                        }
                    }

                    WorkInfo.State.FAILED -> {
                        val message = workInfo.outputData.getString(SyncWorker.KEY_MESSAGE)
                        toast(getString(R.string.info_sync_failed, message))
                    }

                    else -> {
                        val progress = workInfo.progress
                        binding.tvProgress.text = progress.getString(SyncWorker.PARAM_PROGRESS)
                    }
                }
            }
    }
}
