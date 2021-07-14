package com.example.imagesearch.di

import com.example.imagesearch.domain.repository.ApiFactory
import com.example.imagesearch.domain.repository.ImageSearchRepo
import com.example.imagesearch.domain.repository.ImageSearchServices
import com.example.imagesearch.domain.usecase.ImageSearchUseCase
import com.example.imagesearch.domain.util.CustomCoroutineDispatcherProvider
import com.example.imagesearch.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val imageSearchModule = module {

    single {
        ApiFactory.retrofit().create(ImageSearchServices::class.java)
    }
    factory {
        ImageSearchRepo(get())
    }

    single { CustomCoroutineDispatcherProvider() }
    factory {
        ImageSearchUseCase(get(), get())
    }

    viewModel {
        MainViewModel(get())
    }
}