package cz.uhk.fim.attendancapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import cz.uhk.fim.attendancapp.screens.HomeScreen
import cz.uhk.fim.attendancapp.screens.MeetingDetailScreen
import cz.uhk.fim.attendancapp.screens.MeetingsScreen
import cz.uhk.fim.attendancapp.screens.TripDetailScreen
import cz.uhk.fim.attendancapp.screens.TripsScreen
import cz.uhk.fim.attendancapp.ui.theme.AttendancAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zpět")
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
        composable("meetingDetail/{meetingId}") { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId")?.toIntOrNull()
            MeetingDetailScreen(meetingId, navController)
        }
        composable("tripDetail/{tripId}") { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId")?.toIntOrNull()
            TripDetailScreen(tripId, navController)
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