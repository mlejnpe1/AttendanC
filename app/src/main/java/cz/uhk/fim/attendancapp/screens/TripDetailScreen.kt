package cz.uhk.fim.attendancapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import cz.uhk.fim.attendancapp.BuildConfig
import cz.uhk.fim.attendancapp.viewmodel.TripsViewModel
import cz.uhk.fim.attendancapp.viewmodel.WeatherViewModel
import okhttp3.internal.wait
import org.koin.androidx.compose.koinViewModel
import kotlin.math.max

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
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        if (trip != null) {
            Text(
                text = "📋 Detail výpravy",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${trip.title}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "📅 Datum: ${trip.date}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "📍 Místo: ${trip.location}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "🌤️ Předpověď počasí",
                        style = MaterialTheme.typography.titleMedium
                    )
                    when {
                        forecast == null -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Text(
                                    "Načítání předpovědi...",
                                    modifier = Modifier.padding(top = 8.dp),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        else -> {
                            val firstForecast = forecast?.list?.firstOrNull()
                            if (firstForecast != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    AsyncImage(
                                        model = "https://openweathermap.org/img/wn/${firstForecast.weather.firstOrNull()?.icon}@2x.png",
                                        contentDescription = "Ikonka počasí",
                                        modifier = Modifier.size(64.dp)
                                    )

                                    Column {
                                        Text(
                                            "🌡️ ${firstForecast.main.temp}°C",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            "💨 Vítr: ${firstForecast.wind.speed} m/s",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            "☁️ ${firstForecast.weather.firstOrNull()?.description ?: "Neznámé"}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    "Předpověď není k dispozici.",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                "Výprava nebyla nalezena",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}