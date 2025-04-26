package cz.uhk.fim.attendancapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.uhk.fim.attendancapp.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "trips_datastore"
val Context.tripsDataStore by preferencesDataStore(name = DATASTORE_NAME)

class TripsDataStore(private val context: Context) {
    private val gson = Gson()
    private val TRIPS_KEY = stringPreferencesKey("trips_list")

    fun getTrips(): Flow<List<Trip>> {
        return context.tripsDataStore.data.map { preferences ->
            val tripsJson = preferences[TRIPS_KEY]
            if (tripsJson.isNullOrEmpty()) {
                emptyList()
            } else {
                val type = object : TypeToken<List<Trip>>() {}.type
                gson.fromJson(tripsJson, type)
            }
        }
    }

    suspend fun saveTrips(trips: List<Trip>) {
        val tripsJson = gson.toJson(trips)
        context.tripsDataStore.edit { preferences ->
            preferences[TRIPS_KEY] = tripsJson
        }
    }
}
