package cz.uhk.fim.attendancapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.uhk.fim.attendancapp.model.Meeting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "meetings_datastore"
val Context.meetingsDataStore by preferencesDataStore(name = DATASTORE_NAME)

class MeetingsDataStore(private val context: Context) {
    private val gson = Gson()
    private val MEETINGS_KEY = stringPreferencesKey("meetings_list")

    fun getMeetings(): Flow<List<Meeting>> {
        return context.meetingsDataStore.data.map { preferences ->
            val meetingsJson = preferences[MEETINGS_KEY]
            if (meetingsJson.isNullOrEmpty()) {
                emptyList()
            } else {
                val type = object : TypeToken<List<Meeting>>() {}.type
                gson.fromJson(meetingsJson, type)
            }
        }
    }

    suspend fun saveMeetings(meetings: List<Meeting>) {
        val meetingsJson = gson.toJson(meetings)
        context.meetingsDataStore.edit { preferences ->
            preferences[MEETINGS_KEY] = meetingsJson
        }
    }
}