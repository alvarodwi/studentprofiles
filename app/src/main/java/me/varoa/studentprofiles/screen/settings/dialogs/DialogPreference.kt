package me.varoa.studentprofiles.screen.settings.dialogs

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.varoa.studentprofiles.core.data.prefs.DataStoreManager
import org.koin.mp.KoinPlatformTools

open class DialogPreference
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : Preference(context, attrs) {
        protected val prefs by lazy { KoinPlatformTools.defaultContext().get().get<DataStoreManager>() }
        private var isShowing = false
        var customSummary: String? = null

        override fun onClick() {
            if (!isShowing) {
                dialog().apply {
                    setOnDismissListener { this@DialogPreference.isShowing = false }
                }
                    .show()
            }
            isShowing = true
        }

        override fun getSummary(): CharSequence? {
            return customSummary ?: super.getSummary()
        }

        open fun dialog(): MaterialAlertDialogBuilder {
            return MaterialAlertDialogBuilder(context).apply {
                if (title != null) {
                    setTitle(title)
                }
                setNegativeButton(context.getString(android.R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
            }
        }
    }
