package cz.uhk.fim.attendancapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.uhk.fim.attendancapp.model.Participant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "participants_datastore"
val Context.participantsDataStore by preferencesDataStore(name = DATASTORE_NAME)

class ParticipantsDataStore (private val context: Context){
    private val gson = Gson()
    private val PARTICIPANTS_KEY = stringPreferencesKey("participants_list")

    fun getParticipants(): Flow<List<Participant>>{
        return context.participantsDataStore.data.map { preferences ->
            val participantsJson = preferences[PARTICIPANTS_KEY]
            if (participantsJson.isNullOrEmpty()){
                emptyList()
            }else{
                val type = object : TypeToken<List<Participant>>() {}.type
                gson.fromJson(participantsJson, type)
            }
        }
    }

    suspend fun saveParticipants(participants: List<Participant>){
        val participantsJson = gson.toJson(participants)
        context.participantsDataStore.edit { preferences ->
            preferences[PARTICIPANTS_KEY] = participantsJson
        }
    }
}