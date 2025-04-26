package cz.uhk.fim.attendancapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.attendancapp.model.Trip
import cz.uhk.fim.attendancapp.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripsViewModel (private val repository: TripRepository) : ViewModel(){
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val meetings: StateFlow<List<Trip>> = _trips

    fun loadMeetings(){
        viewModelScope.launch {
            _trips.value = repository.getTrips()
        }
    }

    fun getMeetingById(id: Int): Trip? {
        return repository.getTripById(id)
    }
}