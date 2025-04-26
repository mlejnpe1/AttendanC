package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.uhk.fim.attendancapp.model.Meeting

@Composable
fun MeetingsScreen(navController: NavHostController){
    val mockMeetings = listOf(
        Meeting(1, "Schůzka vlčat", "2025-04-30", "Herní schůzka v klubovně"),
        Meeting(2, "Schůzka světlušek", "2025-05-05", "Tvořivá dílna"),
        Meeting(3, "Společná schůzka", "2025-05-12", "Výprava do přírody")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Seznam schůzek", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(mockMeetings) { meeting ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable{ navController.navigate("meetingDetail/${meeting.id}")},
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(meeting.title, style = MaterialTheme.typography.titleLarge)
                        Text("Datum: ${meeting.date}")
                        Text("Popis: ${meeting.description}")
                    }
                }
            }
        }
    }
}