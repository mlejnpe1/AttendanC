package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.uhk.fim.attendancapp.viewmodel.TripsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TripsScreen(navController: NavHostController){
    val viewModel: TripsViewModel = koinViewModel()
    val trips by viewModel.trips.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect (Unit){
        viewModel.loadTrips()
    }

    Scaffold (
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 64.dp)
            )
        }},
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Přidat výpravu")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            Text("Seznam výprav", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(trips) { trip ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { navController.navigate("tripDetail/${trip.id}") },
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

    if (showDialog) {
        AddTripDialog(
            onDismiss = { showDialog = false },
            onSave = { newTrip ->
                viewModel.saveTrips(trips + newTrip)
                showDialog = false
                showSnackbar = true
            },
            existingTrips = trips,
        )
    }

    if(showSnackbar){
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("Výprava byla úspěšně přidána!")
            showSnackbar = false
        }
    }

}