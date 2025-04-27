package cz.uhk.fim.attendancapp.repository

import cz.uhk.fim.attendancapp.data.remote.api.WeatherApi
import cz.uhk.fim.attendancapp.data.remote.model.WeatherResponse

class WeatherRepository(private val weatherApi: WeatherApi) {
    suspend fun getForecast(lat: Double, lon: Double, apiKey: String): WeatherResponse {
        println("Calling API with: lat=$lat, lon=$lon, key=$apiKey")
        return weatherApi.getWeatherForecast(lat, lon, apiKey)
    }
}