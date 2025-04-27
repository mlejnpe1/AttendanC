package cz.uhk.fim.attendancapp.di

import cz.uhk.fim.attendancapp.data.local.MeetingsDataStore
import cz.uhk.fim.attendancapp.data.local.ParticipantsDataStore
import cz.uhk.fim.attendancapp.data.local.TripsDataStore
import cz.uhk.fim.attendancapp.data.remote.api.WeatherApi
import cz.uhk.fim.attendancapp.repository.MeetingsRepository
import cz.uhk.fim.attendancapp.repository.ParticipantsRepository
import cz.uhk.fim.attendancapp.repository.TripsRepository
import cz.uhk.fim.attendancapp.repository.WeatherRepository
import cz.uhk.fim.attendancapp.viewmodel.MeetingsViewModel
import cz.uhk.fim.attendancapp.viewmodel.ParticipantsViewModel
import cz.uhk.fim.attendancapp.viewmodel.TripsViewModel
import cz.uhk.fim.attendancapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module{
    single { TripsDataStore(get()) }
    single { TripsRepository(get()) }
    single { MeetingsDataStore(get()) }
    single { MeetingsRepository(get()) }
    single { ParticipantsDataStore(get()) }
    single { ParticipantsRepository(get()) }
    single { WeatherRepository(get()) }

    viewModel { TripsViewModel(get()) }
    viewModel { MeetingsViewModel(get(), get()) }
    viewModel { ParticipantsViewModel(get()) }
    viewModel { WeatherViewModel(get()) }
}

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}