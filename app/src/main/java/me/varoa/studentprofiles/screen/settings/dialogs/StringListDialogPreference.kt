package me.varoa.studentprofiles.screen.settings.dialogs

import android.content.Context
import android.util.AttributeSet
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StringListDialogPreference
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : DialogPreference(context, attrs) {
        private var entries = emptyList<String>()
        var entryValues = emptyList<String>()
        private var defValue: String = ""
        var entriesRes
            get() = emptyList<Int>()
            set(value) {
                entries = value.map { context.getString(it) }
            }

        override fun onSetInitialValue(defaultValue: Any?) {
            super.onSetInitialValue(defaultValue)
            defValue = defaultValue as? String ?: defValue
        }

        override fun getSummary(): CharSequence? {
            return if (customSummary != null) {
                customSummary!!
            } else {
                (
                    if (key == null) {
                        super.getSummary()
                    } else {
                        val index =
                            entryValues.indexOf(
                                runBlocking {
                                    prefs.get(stringPreferencesKey(key), defValue)
                                        .first()
                                },
                            )
                        if (entries.isEmpty() || index == -1) {
                            ""
                        } else {
                            entries[index]
                        }
                    }
                    )
            }
        }

        override fun dialog(): MaterialAlertDialogBuilder {
            return super.dialog()
                .apply {
                    val default =
                        entryValues.indexOf(
                            runBlocking {
                                prefs.get(stringPreferencesKey(key), defValue).first()
                            },
                        )
                    setSingleChoiceItems(entries.toTypedArray(), default) { dialog, which ->
                        val value = entryValues[which]
                        if (key != null) {
                            runBlocking {
                                prefs.set(stringPreferencesKey(key), value)
                            }
                        }
                        callChangeListener(value)
                        this@StringListDialogPreference.summary = this@StringListDialogPreference.summary
                        dialog.dismiss()
                    }
                }
        }
    }
