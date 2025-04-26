package cz.uhk.fim.attendancapp.repository

import cz.uhk.fim.attendancapp.model.Trip

class TripsRepository {
    private val trips = listOf(
        Trip(1, "Výprava na Sněžku", "2025-05-10", "Krkonoše"),
        Trip(2, "Tábor u Berounky", "2025-07-01", "Berounka"),
        Trip(3, "Výprava do Brd", "2025-09-15", "Brdy")
    )

    fun getTrips(): List<Trip> = trips

    fun getTripById(id: Int): Trip? = trips.find { it.id == id }
}