package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.uhk.fim.attendancapp.viewmodel.TripsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TripDetailScreen(tripId: Int, navController: NavHostController) {
    val viewModel: TripsViewModel = koinViewModel()
    val trips by viewModel.trips.collectAsState()
    val trip = trips.find { it.id == tripId }

    LaunchedEffect(Unit) {
        viewModel.loadTrips()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (trip != null){
            Text(
                text = "Detail výpravy",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Název: ${trip.title}", style = MaterialTheme.typography.titleLarge)
            Text("Datum: ${trip.date}")
            Text("Místo: ${trip.location}")
        }else{
            Text("Výprava nebyla nalezena", style = MaterialTheme.typography.bodyLarge)
        }
    }
}