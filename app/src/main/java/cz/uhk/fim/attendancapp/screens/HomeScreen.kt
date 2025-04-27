package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "👋 Vítej v aplikaci AttendanC!",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Spravuj schůzky, výpravy a účastníky pohodlně na jednom místě.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            HomeActionButton(
                text = "Schůzky",
                icon = Icons.Default.DateRange,
                onClick = { navController.navigate("meetings") }
            )
            HomeActionButton(
                text = "Výpravy",
                icon = Icons.Default.LocationOn,
                onClick = { navController.navigate("trips") }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "⚜\uFE0F Buď připraven!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun HomeActionButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text(text = text, color = MaterialTheme.colorScheme.onPrimary)
    }
}