package me.varoa.studentprofiles.screen.sync

import android.os.Bundle
import android.view.View
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
                            viewLifecycleOwner.lifecycleScope.launch {
                                prefs.finishFirstTimeSync()
                                findNavController().navigate(
                                    SyncFragmentDirections.actionSyncToHome(),
                                )
                            }
                        }
                    }
            }
        }
    }
}
