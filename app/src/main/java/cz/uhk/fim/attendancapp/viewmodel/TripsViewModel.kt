package cz.uhk.fim.attendancapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.attendancapp.model.Trip
import cz.uhk.fim.attendancapp.repository.TripsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripsViewModel (private val repository: TripsRepository) : ViewModel(){
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips

    fun loadTrips(){
        viewModelScope.launch {
            _trips.value = repository.getTrips()
        }
    }

    fun getTripById(id: Int): Trip? {
        return repository.getTripById(id)
    }
}