package me.varoa.studentprofiles.screen.settings

import androidx.core.graphics.drawable.DrawableCompat
import androidx.preference.Preference
import androidx.preference.PreferenceGroup
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import me.varoa.studentprofiles.screen.settings.dialogs.StringListDialogPreference

@DslMarker
@Target(AnnotationTarget.TYPE)
annotation class SettingsDsl

inline fun PreferenceGroup.preference(block: (@SettingsDsl Preference).() -> Unit): Preference {
    return initThenAdd(Preference(context), block)
}

inline fun PreferenceGroup.stringListPreference(
    block: (@SettingsDsl StringListDialogPreference).() -> Unit
): StringListDialogPreference {
    return initThenAdd(StringListDialogPreference(context), block)
}

inline fun <P : Preference> PreferenceGroup.initThenAdd(
    p: P,
    block: P.() -> Unit
): P {
    return p.apply {
        block()
        this.isIconSpaceReserved = false
        addPreference(this)
    }
}

inline fun Preference.onClick(crossinline block: () -> Unit) {
    setOnPreferenceClickListener { block(); true }
}

inline fun Preference.onChange(crossinline block: (Any?) -> Boolean) {
    setOnPreferenceChangeListener { _, newValue -> block(newValue) }
}

var Preference.defaultValue: Any?
    get() = null // set only
    set(value) {
        setDefaultValue(value)
    }

var Preference.titleRes: Int
    get() = 0 // set only
    set(value) {
        setTitle(value)
    }
