package cz.uhk.fim.attendancapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.attendancapp.data.remote.model.WeatherResponse
import cz.uhk.fim.attendancapp.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _forecast = MutableStateFlow<WeatherResponse?>(null)
    val forecast: StateFlow<WeatherResponse?> = _forecast

    fun loadForecast(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getForecast(lat, lon, apiKey)
                _forecast.value = response
                Log.d("WeatherViewModel", "Forecast loaded successfully: $response")
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error loading forecast: ${e.message}", e)
            }
        }
    }
}