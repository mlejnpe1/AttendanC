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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import cz.uhk.fim.attendancapp.model.Meeting
import cz.uhk.fim.attendancapp.model.MeetingParticipant
import cz.uhk.fim.attendancapp.viewmodel.MeetingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MeetingsScreen(navController: NavHostController) {
    val viewModel: MeetingsViewModel = koinViewModel()
    val meetings by viewModel.meetings.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var meetingToDelete by remember { mutableStateOf<Meeting?>(null) }
    var meetingToEdit by remember { mutableStateOf<Meeting?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadMeetings()
    }

    Scaffold(
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 64.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                meetingToEdit = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Přidat schůzku")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            Text("Seznam schůzek", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(meetings) { meeting ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { navController.navigate("meetingDetail/${meeting.id}") },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = meeting.title,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = "📅 Datum: ${meeting.date}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Text(
                                text = "📝 Popis: ${meeting.description}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Text(
                                text = "👥 Účast: ${meeting.participants.count { it.isPresent }}/${meeting.participants.size}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    meetingToEdit = meeting
                                    showDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Upravit schůzku",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = {
                                    meetingToDelete = meeting
                                    showDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Smazat schůzku",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddMeetingDialog(
            onDismiss = { showDialog = false },
            onSave = { newMeeting ->
                val participants = viewModel.participants.map { participant ->
                    MeetingParticipant(participantId = participant.id, isPresent = false)
                }
                val meetingWithParticipants = if (newMeeting.participants.isEmpty()) {
                    newMeeting.copy(participants = participants)
                } else {
                    newMeeting
                }

                val updatedMeetings = meetings.map { existingMeeting ->
                    if (existingMeeting.id == meetingWithParticipants.id) meetingWithParticipants else existingMeeting
                }
                val finalMeetings = if (meetings.any { it.id == meetingWithParticipants.id }) {
                    updatedMeetings
                } else {
                    meetings + meetingWithParticipants
                }
                viewModel.saveMeetings(finalMeetings)
                showDialog = false
                meetingToEdit = null
                showSnackbar = true
            },
            existingMeetings = meetings,
            existingMeeting = meetingToEdit
        )
    }

    if (showSnackbar) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("Schůzka byla úspěšně uložena!")
            showSnackbar = false
        }
    }

    if (showDialog && meetingToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Smazat schůzku") },
            text = { Text("Opravdu chceš smazat schůzku '${meetingToDelete!!.title}'?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveMeetings(meetings.filter { it.id != meetingToDelete!!.id })
                    showDialog = false
                    meetingToDelete = null
                }) {
                    Text("Smazat")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    meetingToDelete = null
                }) {
                    Text("Zrušit")
                }
            }
        )
    }
}