package cz.uhk.fim.attendancapp.repository

import cz.uhk.fim.attendancapp.data.local.MeetingsDataStore
import cz.uhk.fim.attendancapp.model.Meeting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class MeetingsRepository(private val dataStore: MeetingsDataStore) {
    fun getMeetings(): Flow<List<Meeting>> = dataStore.getMeetings()

    suspend fun saveMeetings(meetings: List<Meeting>) {
        println("Saving meetings: $meetings")
        dataStore.saveMeetings(meetings)
    }

    suspend fun updateAttendance(meetingId: Int, participantId: Int, isPresent: Boolean) {
        val meetings = getMeetings().first()
        val updatedMeetings = meetings.map { meeting ->
            if (meeting.id == meetingId) {
                val updatedParticipants = meeting.participants.orEmpty().map { participant ->
                    if (participant.participantId == participantId) {
                        participant.copy(isPresent = isPresent)
                    } else participant
                }
                meeting.copy(participants = updatedParticipants)
            } else meeting
        }
        println("Updated meetings: $updatedMeetings")
        saveMeetings(updatedMeetings)
    }
}