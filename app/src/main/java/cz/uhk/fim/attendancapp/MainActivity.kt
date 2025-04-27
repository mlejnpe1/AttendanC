package cz.uhk.fim.attendancapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.uhk.fim.attendancapp.di.appModule
import cz.uhk.fim.attendancapp.screens.HomeScreen
import cz.uhk.fim.attendancapp.screens.MeetingDetailScreen
import cz.uhk.fim.attendancapp.screens.MeetingsScreen
import cz.uhk.fim.attendancapp.screens.ParticipantsScreen
import cz.uhk.fim.attendancapp.screens.TripDetailScreen
import cz.uhk.fim.attendancapp.screens.TripsScreen
import cz.uhk.fim.attendancapp.ui.theme.AttendancAppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        setContent {
            AttendancAppTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AttendanC") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zpět")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("participants") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Účastníci")
                    }
                }
            )
        }
    ) { innerPadding ->
        Navigation(navController = navController, innerPadding = innerPadding)
    }
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("home") { HomeScreen(navController) }
        composable("meetings") { MeetingsScreen(navController) }
        composable("trips") { TripsScreen(navController) }
        composable("participants") { ParticipantsScreen(navController) }
        composable("meetingDetail/{meetingId}") { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId")?.toIntOrNull()
            if (meetingId != null) {
                MeetingDetailScreen(meetingId = meetingId, navController = navController)
            } else {
                Text("Neplatné ID schůzky")
            }
        }
        composable("tripDetail/{tripId}") { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId")?.toIntOrNull()
            if (tripId != null) {
                TripDetailScreen(tripId = tripId, navController = navController)
            } else {
                Text("Neplatné ID výpravy")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AttendancAppTheme {
        MainScreen(rememberNavController())
    }
}