package com.example.photoweatherapp.di

import com.example.photoweatherapp.ui.history.HistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { HistoryViewModel(get()) }
}