package com.example.imagesearch.di

import com.example.imagesearch.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val imageSearchModule = module {

    viewModel {
        MainViewModel()
    }
}