package me.varoa.studentprofiles.core.data.prefs

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PrefKeys {
    val THEME_KEY = stringPreferencesKey("theme")
    val FIRST_TIME_SYNC = booleanPreferencesKey("first_time_sync")
}
