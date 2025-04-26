package cz.uhk.fim.attendancapp.repository

import cz.uhk.fim.attendancapp.data.local.MeetingsDataStore
import cz.uhk.fim.attendancapp.model.Meeting
import kotlinx.coroutines.flow.Flow

class MeetingsRepository(private val dataStore: MeetingsDataStore) {
    fun getMeetings(): Flow<List<Meeting>> = dataStore.getMeetings()

    suspend fun saveMeetings(meetings: List<Meeting>) {
        dataStore.saveMeetings(meetings)
    }
}