package me.varoa.studentprofiles.core.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.varoa.studentprofiles.core.domain.model.AppTheme

private val Context.dataStore by preferencesDataStore("prefs")

class DataStoreManager(
    appContext: Context,
) {
    private val prefsDataStore = appContext.dataStore

    // generic
    suspend fun <T : Any> set(key: Preferences.Key<T>, value: T) {
        prefsDataStore.edit { prefs -> prefs[key] = value }
    }

    fun <T : Any> get(key: Preferences.Key<T>, default: T): Flow<T?> =
        prefsDataStore.data.map { it[key] ?: default }

    // theme
    val theme
        get() =
            prefsDataStore.data.map { prefs ->
                prefs[PrefKeys.THEME_KEY] ?: AppTheme.DARK.name
            }

    suspend fun setTheme(theme: AppTheme) {
        prefsDataStore.edit { prefs -> prefs[PrefKeys.THEME_KEY] = theme.name }
    }

    // onboarding
    val firstTimeSync
        get() =
            prefsDataStore.data.map { prefs ->
                prefs[PrefKeys.FIRST_TIME_SYNC] ?: false
            }

    suspend fun finishFirstTimeSync() {
        prefsDataStore.edit { prefs -> prefs[PrefKeys.FIRST_TIME_SYNC] = true }
    }
}
