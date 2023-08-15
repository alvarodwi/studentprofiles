package me.varoa.studentprofiles.screen.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.core.data.prefs.PrefKeys
import me.varoa.studentprofiles.core.domain.model.AppTheme
import me.varoa.studentprofiles.core.work.SyncWorker
import me.varoa.studentprofiles.databinding.FragmentSettingsBinding
import me.varoa.studentprofiles.ext.snackbar
import me.varoa.studentprofiles.ext.toggleAppTheme
import me.varoa.studentprofiles.viewbinding.viewBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding by viewBinding<FragmentSettingsBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(toolbar) {
                title = getString(R.string.title_settings)
                setNavigationOnClickListener { findNavController().popBackStack() }
            }

            childFragmentManager.commit {
                replace(settingsContainer.id, SettingsContainer())
            }
        }
    }

    class SettingsContainer : PreferenceFragmentCompat() {
        override fun onCreatePreferences(
            savedInstanceState: Bundle?,
            rootKey: String?,
        ) {
            val screen = preferenceManager.createPreferenceScreen(requireContext())
            preferenceScreen = screen
            setupPreferenceScreen(screen)
        }

        private fun setupPreferenceScreen(screen: PreferenceScreen) =
            with(screen) {
                setTitle(R.string.title_settings)

                preferenceCategory {
                    title = getString(R.string.prefs_category_general)

                    stringListPreference {
                        key = PrefKeys.THEME_KEY.name
                        titleRes = R.string.prefs_theme
                        entriesRes =
                            listOf(
                                R.string.prefs_theme_light,
                                R.string.prefs_theme_dark,
                                R.string.prefs_theme_system,
                            )
                        entryValues =
                            listOf(
                                AppTheme.LIGHT.name,
                                AppTheme.DARK.name,
                                AppTheme.SYSTEM.name,
                            )
                        defaultValue = AppTheme.SYSTEM.name
                        onChange {
                            toggleAppTheme(it as String)
                            requireActivity().recreate()
                            true
                        }
                    }
                }

                preferenceCategory {
                    title = getString(R.string.prefs_category_sync)

                    preference {
                        titleRes = R.string.prefs_run_sync
                        summary = getString(R.string.prefs_run_sync_summary)
                        onClick {
                            runSync()
                        }
                    }
                }
            }

        private fun runSync() {
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
                        val message = workInfo.outputData.getString(SyncWorker.KEY_MESSAGE)
                        message?.let { snackbar(it) }
                    } else if (workInfo?.state == WorkInfo.State.FAILED) {
                        val message = workInfo.outputData.getString(SyncWorker.KEY_MESSAGE)
                        snackbar(getString(R.string.info_sync_failed, message))
                    }
                }
        }
    }
}
