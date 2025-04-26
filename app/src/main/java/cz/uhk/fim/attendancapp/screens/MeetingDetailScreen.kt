package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.uhk.fim.attendancapp.viewmodel.MeetingsViewModel
import cz.uhk.fim.attendancapp.viewmodel.TripsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MeetingDetailScreen(meetingId: Int?, navController: NavHostController) {
    val viewModel: MeetingsViewModel = koinViewModel()
    val meeting = meetingId?.let { viewModel.getMeetingById(it) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (meeting != null) {
                Text("Detail schůzky", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Název: ${meeting.title}", style = MaterialTheme.typography.titleLarge)
                Text("Datum: ${meeting.date}")
                Text("Popis: ${meeting.description}")
            } else {
                Text("Schůzka nebyla nalezena", style = MaterialTheme.typography.bodyLarge)
            }
        }
}
