package cz.uhk.fim.attendancapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.attendancapp.model.Trip
import cz.uhk.fim.attendancapp.repository.TripsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class TripsViewModel (private val repository: TripsRepository) : ViewModel(){
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips

    fun loadTrips() {
        viewModelScope.launch {
            repository.getTrips().collectLatest { tripList ->
                _trips.value = tripList
            }
        }
    }

    fun getTripById(id: Int): Trip? {
        return _trips.value.find { it.id == id }
    }

    fun saveTrips(trips: List<Trip>) {
        viewModelScope.launch {
            repository.saveTrips(trips)
        }
    }
}