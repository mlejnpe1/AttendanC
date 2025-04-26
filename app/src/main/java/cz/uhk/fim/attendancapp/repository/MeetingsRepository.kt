package cz.uhk.fim.attendancapp.repository

import cz.uhk.fim.attendancapp.model.Meeting

class MeetingsRepository {
    private val meetings = listOf(
        Meeting(1, "Schůzka vlčat", "2025-04-30", "Herní schůzka v klubovně"),
        Meeting(2, "Schůzka světlušek", "2025-05-05", "Tvořivá dílna"),
        Meeting(3, "Společná schůzka", "2025-05-12", "Výprava do přírody")
    )

    fun getMeetings(): List<Meeting> = meetings

    fun getMeetingById(id: Int): Meeting? = meetings.find { it.id == id }
}