package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.uhk.fim.attendancapp.viewmodel.MeetingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MeetingDetailScreen(meetingId: Int, navController: NavHostController) {
    val viewModel: MeetingsViewModel = koinViewModel()
    val meetings by viewModel.meetings.collectAsState()
    val meeting = meetings.find { it.id == meetingId }
    val participants = viewModel.participants

    LaunchedEffect(Unit) {
        viewModel.loadMeetings()
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (meeting != null) {
                Text(
                    text = "Detail schůzky",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Název: ${meeting.title}", style = MaterialTheme.typography.titleLarge)
                Text("Datum: ${meeting.date}")
                Text("Popis: ${meeting.description}")
                Spacer(modifier = Modifier.height(16.dp))

                Text("Účastníci:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                val participantsList = meeting.participants ?: emptyList()

                participants.forEach { participant ->
                    val isPresent = participantsList.find { it.participantId == participant.id }?.isPresent ?: false

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = isPresent,
                            onCheckedChange = { checked ->
                                viewModel.updateAttendance(meeting.id, participant.id, checked)
                            }
                        )
                        Text(text = participant.name)
                    }
                }
            } else {
                Text("Schůzka nebyla nalezena", style = MaterialTheme.typography.bodyLarge)
            }
        }
}
