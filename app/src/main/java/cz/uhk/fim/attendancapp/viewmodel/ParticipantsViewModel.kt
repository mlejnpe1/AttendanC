package cz.uhk.fim.attendancapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.attendancapp.model.Participant
import cz.uhk.fim.attendancapp.repository.ParticipantsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ParticipantsViewModel(private val participantsRepository: ParticipantsRepository) : ViewModel() {
    private val _participants = MutableStateFlow<List<Participant>>(emptyList())
    val participants: StateFlow<List<Participant>> = _participants

    init {
        loadParticipants()
    }

    fun loadParticipants() {
        viewModelScope.launch {
            participantsRepository.getAllParticipants().collectLatest { participantList ->
                _participants.value = participantList
            }
        }
    }

    fun addParticipants(newParticipant: Participant) {
        viewModelScope.launch {
            val currentParticipants = _participants.value
            val updatedParticipants = currentParticipants + newParticipant
            participantsRepository.saveParticipants(updatedParticipants)
        }
    }

    fun deleteParticipant(participantId: Int) {
        viewModelScope.launch {
            val updatedParticipants = _participants.value.filter { it.id != participantId }
            participantsRepository.saveParticipants(updatedParticipants)
        }
    }
}