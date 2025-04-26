package cz.uhk.fim.attendancapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.attendancapp.model.Meeting
import cz.uhk.fim.attendancapp.repository.MeetingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MeetingsViewModel(private val repository: MeetingRepository) : ViewModel() {
    private val _meetings = MutableStateFlow<List<Meeting>>(emptyList())
    val meetings: StateFlow<List<Meeting>> = _meetings

    fun loadMeetings(){
        viewModelScope.launch {
            _meetings.value = repository.getMeetings()
        }
    }

    fun getMeetingById(id: Int): Meeting? {
        return repository.getMeetingById(id)
    }
}