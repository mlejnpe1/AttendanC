package cz.uhk.fim.attendancapp.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.uhk.fim.attendancapp.viewmodel.MeetingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MeetingDetailScreen(meetingId: Int, navController: NavHostController) {
    val viewModel: MeetingsViewModel = koinViewModel()
    val meetings by viewModel.meetings.collectAsState()
    val meeting = meetings.find { it.id == meetingId }
    val participants by viewModel.participants.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadMeetings()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        if (meeting != null) {
            Text(
                text = "📋 Detail schůzky",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(meeting.title, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("📅 Datum: ${meeting.date}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "📝 Popis: ${meeting.description}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("👥 Účastníci", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            val participantsList = meeting.participants ?: emptyList()

            participants.forEach { participant ->
                val isPresent =
                    participantsList.find { it.participantId == participant.id }?.isPresent ?: false

                val backgroundColor by animateColorAsState(
                    targetValue = if (isPresent) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
                val scale by animateFloatAsState(targetValue = if (isPresent) 1.01f else 1f)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scale)
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = isPresent,
                        onCheckedChange = { checked ->
                            viewModel.updateAttendance(meeting.id, participant.id, checked)
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = participant.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        } else {
            Text(
                text = "❌ Schůzka nebyla nalezena",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
