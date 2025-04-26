package cz.uhk.fim.attendancapp.repository

import cz.uhk.fim.attendancapp.data.local.TripsDataStore
import cz.uhk.fim.attendancapp.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class TripsRepository (private val tripsDataStore: TripsDataStore){
    fun getTrips(): Flow<List<Trip>> = tripsDataStore.getTrips()

    suspend fun saveTrips(trips: List<Trip>) {
        tripsDataStore.saveTrips(trips)
    }
}