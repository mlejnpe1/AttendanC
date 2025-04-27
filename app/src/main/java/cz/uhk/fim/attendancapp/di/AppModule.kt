package cz.uhk.fim.attendancapp.di

import cz.uhk.fim.attendancapp.data.local.MeetingsDataStore
import cz.uhk.fim.attendancapp.data.local.ParticipantsDataStore
import cz.uhk.fim.attendancapp.data.local.TripsDataStore
import cz.uhk.fim.attendancapp.repository.MeetingsRepository
import cz.uhk.fim.attendancapp.repository.ParticipantsRepository
import cz.uhk.fim.attendancapp.repository.TripsRepository
import cz.uhk.fim.attendancapp.viewmodel.MeetingsViewModel
import cz.uhk.fim.attendancapp.viewmodel.ParticipantsViewModel
import cz.uhk.fim.attendancapp.viewmodel.TripsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    single { TripsDataStore(get()) }
    single { TripsRepository(get()) }
    single { MeetingsDataStore(get()) }
    single { MeetingsRepository(get()) }
    single { ParticipantsDataStore(get()) }
    single { ParticipantsRepository(get()) }

    viewModel { TripsViewModel(get()) }
    viewModel { MeetingsViewModel(get(), get()) }
    viewModel { ParticipantsViewModel(get()) }
}