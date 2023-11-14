package com.waff1e.waffle.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.waff1e.waffle.di.LoginUserPreferenceModule.PreferenceKeys.KEY_JSESSIONID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val LOGIN_USER_NAME = "login_user"
const val JSESSIONID_NAME = "jsessionid"
const val JSESSIONID = "JSESSIONID"

@Singleton
class LoginUserPreferenceModule @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = LOGIN_USER_NAME)

    val jsessionidFlow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            preferences[KEY_JSESSIONID]
        }

    suspend fun setJSessionId(id: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_JSESSIONID] = id
        }
    }

    suspend fun removeJSESSIONID() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_JSESSIONID)
        }
    }

    private object PreferenceKeys {
        val KEY_JSESSIONID = stringPreferencesKey(JSESSIONID_NAME)
    }
}