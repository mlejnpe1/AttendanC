package cz.uhk.fim.attendancapp.repository

import cz.uhk.fim.attendancapp.data.local.ParticipantsDataStore
import cz.uhk.fim.attendancapp.model.Participant
import kotlinx.coroutines.flow.Flow

class ParticipantsRepository (private val dataStore: ParticipantsDataStore){
    fun getAllParticipants(): Flow<List<Participant>> = dataStore.getParticipants()

    suspend fun saveParticipants(participants: List<Participant>){
        dataStore.saveParticipants(participants)
    }
}