package me.varoa.studentprofiles.screen.sync

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.core.work.SyncWorker
import me.varoa.studentprofiles.databinding.FragmentSyncBinding
import me.varoa.studentprofiles.ext.snackbar
import me.varoa.studentprofiles.viewbinding.viewBinding

class SyncFragment : Fragment(R.layout.fragment_sync) {
    private val binding by viewBinding<FragmentSyncBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSync.setOnClickListener {
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
                            findNavController()
                        }
                    }
            }
        }
    }
}
