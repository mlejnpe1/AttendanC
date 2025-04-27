package cz.uhk.fim.attendancapp.repository

import cz.uhk.fim.attendancapp.model.Participant

class ParticipantsRepository {
    private val participants = listOf(
        Participant(1, "Alice"),
        Participant(2, "Bob"),
        Participant(3, "Charlie"),
        Participant(4, "David"),
        Participant(5, "Eva")
    )

    fun getAllParticipants(): List<Participant> = participants
}