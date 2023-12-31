package me.varoa.studentprofiles.core.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.varoa.studentprofiles.core.domain.model.AppTheme
import me.varoa.studentprofiles.core.domain.model.SyncInterval

private val Context.DATA_STORE by preferencesDataStore("prefs")

class DataStoreManager(
    appContext: Context,
) {
    private val prefsDataStore = appContext.DATA_STORE

    // generic
    suspend fun <T : Any> set(
        key: Preferences.Key<T>,
        value: T,
    ) {
        prefsDataStore.edit { prefs -> prefs[key] = value }
    }

    fun <T : Any> get(
        key: Preferences.Key<T>,
        default: T,
    ): Flow<T?> = prefsDataStore.data.map { it[key] ?: default }

    // theme
    val theme
        get() =
            prefsDataStore.data.map { prefs ->
                prefs[PrefKeys.THEME_KEY] ?: AppTheme.SYSTEM.name
            }

    // onboarding
    val firstTimeSync
        get() =
            prefsDataStore.data.map { prefs ->
                prefs[PrefKeys.FIRST_TIME_SYNC] ?: true
            }

    val syncInterval
        get() =
            prefsDataStore.data.map { prefs ->
                prefs[PrefKeys.SYNC_INTERVAL] ?: SyncInterval.WEEKLY.name
            }

    suspend fun finishFirstTimeSync() {
        prefsDataStore.edit { prefs -> prefs[PrefKeys.FIRST_TIME_SYNC] = false }
    }
}
