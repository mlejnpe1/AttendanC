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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.uhk.fim.attendancapp.model.Trip

@Composable
fun TripsScreen(navController: NavHostController){
    val mockTrips = listOf(
        Trip(1, "Výprava na Sněžku", "2025-05-10", "Krkonoše"),
        Trip(2, "Tábor u Berounky", "2025-07-01", "Berounka"),
        Trip(3, "Výprava do Brd", "2025-09-15", "Brdy")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Seznam výprav", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(mockTrips) { trip ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {navController.navigate("tripDetail/${trip.id}")},
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(trip.title, style = MaterialTheme.typography.titleLarge)
                        Text("Datum: ${trip.date}")
                        Text("Místo: ${trip.location}")
                    }
                }
            }
        }
    }
}