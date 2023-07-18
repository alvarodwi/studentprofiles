package me.varoa.studentprofiles.core.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import me.varoa.studentprofiles.core.domain.model.AppTheme

private val Context.dataStore by preferencesDataStore("prefs")

class DataStoreManager(
    appContext: Context
) {
    private val prefsDataStore = appContext.dataStore

    // theme
    val theme
        get() = prefsDataStore.data.map { prefs ->
            prefs[PrefKeys.THEME_KEY] ?: AppTheme.DARK.name
        }

    suspend fun setTheme(theme: AppTheme) {
        prefsDataStore.edit { prefs -> prefs[PrefKeys.THEME_KEY] = theme.name }
    }
}
