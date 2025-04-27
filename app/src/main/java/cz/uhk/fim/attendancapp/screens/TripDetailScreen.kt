package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import cz.uhk.fim.attendancapp.BuildConfig
import cz.uhk.fim.attendancapp.viewmodel.TripsViewModel
import cz.uhk.fim.attendancapp.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TripDetailScreen(tripId: Int, navController: NavHostController) {
    val tripsViewModel: TripsViewModel = koinViewModel()
    val weatherViewModel: WeatherViewModel = koinViewModel()

    val trips by tripsViewModel.trips.collectAsState()
    val trip = trips.find { it.id == tripId }
    val forecast by weatherViewModel.forecast.collectAsState()


    LaunchedEffect(tripId) {
        weatherViewModel.loadForecast(
            BuildConfig.LAT.toDouble(),
            BuildConfig.LON.toDouble(),
            BuildConfig.WEATHER_API_KEY
        )
        tripsViewModel.loadTrips()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (trip != null) {
            Text("Detail výpravy", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("📌 Název: ${trip.title}", style = MaterialTheme.typography.titleLarge)
            Text("📅 Datum: ${trip.date}", style = MaterialTheme.typography.bodyLarge)
            Text("📍 Místo: ${trip.location}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))

            when {
                forecast == null -> {
                    CircularProgressIndicator()
                    Text("Načítání předpovědi...", modifier = Modifier.padding(top = 8.dp))
                }
                else -> {
                    val firstForecast = forecast?.list?.firstOrNull()
                    if (firstForecast != null) {
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${firstForecast.weather.firstOrNull()?.icon}@2x.png",
                            contentDescription = "Ikonka počasí",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("🌡️ Teplota: ${firstForecast.main.temp}°C", style = MaterialTheme.typography.titleMedium)
                        Text("💨 Vítr: ${firstForecast.wind.speed} m/s", style = MaterialTheme.typography.bodyMedium)
                        Text("🌥️ Popis: ${firstForecast.weather.firstOrNull()?.description ?: "Neznámé"}", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Text("Předpověď není k dispozici.", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        } else {
            Text("Výprava nebyla nalezena", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
        }
    }
}