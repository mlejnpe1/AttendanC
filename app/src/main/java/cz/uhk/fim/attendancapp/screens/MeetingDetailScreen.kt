package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun MeetingDetailScreen(meetingId: Int?, navController: NavHostController) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Detail schůzky",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("ID schůzky: $meetingId", style = MaterialTheme.typography.headlineSmall)
            // Tady můžeš načítat podle ID schůzku z Repositáře / ViewModelu (zatím jen ID)
        }
}
