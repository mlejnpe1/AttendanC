package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.uhk.fim.attendancapp.model.Trip
import cz.uhk.fim.attendancapp.viewmodel.TripsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TripsScreen(navController: NavHostController){
    val viewModel: TripsViewModel = koinViewModel()
    val trips by viewModel.trips.collectAsState(initial = emptyList())
    var showAddEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }
    var tripToEdit by remember { mutableStateOf<Trip?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect (Unit){
        viewModel.loadTrips()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                tripToEdit = null
                showAddEditDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Přidat výpravu")
            }
        },
        bottomBar = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
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
                    TripCard(
                        trip = trip,
                        navController = navController,
                        onEdit = {
                            tripToEdit = trip
                             showAddEditDialog = true
                        },
                        onDelete = {
                            tripToDelete = trip
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showAddEditDialog) {
        AddTripDialog(
            onDismiss = { showAddEditDialog = false },
            onSave = { newTrip ->
                val updatedTrips = trips.map { existingTrip ->
                    if (existingTrip.id == newTrip.id) newTrip else existingTrip
                }
                val finalTrips = if (trips.any { it.id == newTrip.id }) {
                    updatedTrips
                } else {
                    trips + newTrip
                }
                viewModel.saveTrips(finalTrips)
                showAddEditDialog = false
                tripToEdit = null
                showSnackbar = true
            },
            existingTrips = trips,
            existingTrip = tripToEdit
        )
    }

    if (showSnackbar) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Výprava byla úspěšně přidána!")
                showSnackbar = false
            }
        }
    }

    if (tripToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                tripToDelete = null
            },
            title = { Text("Smazat výpravu") },
            text = { Text("Opravdu chceš smazat výpravu '${tripToDelete!!.title}'?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveTrips(trips.filter { it.id != tripToDelete!!.id })
                    showDeleteDialog = false
                    tripToDelete = null
                }) {
                    Text("Smazat")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    tripToDelete = null
                }) {
                    Text("Zrušit")
                }
            }
        )
    }
}

@Composable
fun TripCard(
    trip: Trip,
    navController: NavHostController,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("tripDetail/${trip.id}") },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = trip.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "📅 Datum: ${trip.date}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "📍 Místo: ${trip.location}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Upravit výpravu",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Smazat výpravu",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}