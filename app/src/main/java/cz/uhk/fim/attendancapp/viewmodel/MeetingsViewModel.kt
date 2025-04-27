package cz.uhk.fim.attendancapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.attendancapp.model.Meeting
import cz.uhk.fim.attendancapp.model.Participant
import cz.uhk.fim.attendancapp.repository.MeetingsRepository
import cz.uhk.fim.attendancapp.repository.ParticipantsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MeetingsViewModel(
    private val repository: MeetingsRepository,
    private val participantsRepository: ParticipantsRepository
) : ViewModel() {

    private val _meetings = MutableStateFlow<List<Meeting>>(emptyList())
    val meetings: StateFlow<List<Meeting>> = _meetings

    val _participants = MutableStateFlow<List<Participant>>(emptyList())
    val participants: StateFlow<List<Participant>> = _participants

    init {
        loadMeetings()
        loadParticipants()
    }

    fun loadMeetings() {
        viewModelScope.launch {
            repository.getMeetings().collectLatest { meetingList ->
                _meetings.value = meetingList
            }
        }
    }

    private fun loadParticipants() {
        viewModelScope.launch {
            participantsRepository.getAllParticipants().collectLatest { participantList ->
                _participants.value = participantList
            }
        }
    }

    fun updateAttendance(meetingId: Int, participantId: Int, isPresent: Boolean) {
        viewModelScope.launch {
            repository.updateAttendance(meetingId, participantId, isPresent)
            loadMeetings()
        }
    }

    fun getMeetingById(id: Int): Meeting? {
        return _meetings.value.find { it.id == id }
    }

    fun saveMeetings(newMeetings: List<Meeting>) {
        viewModelScope.launch {
            repository.saveMeetings(newMeetings)
        }
    }
}