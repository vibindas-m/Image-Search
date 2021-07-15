package com.example.imagesearch.di

import com.example.imagesearch.domain.repository.ApiFactory
import com.example.imagesearch.domain.repository.ImageSearchRepo
import com.example.imagesearch.domain.repository.ImageSearchServices
import com.example.imagesearch.domain.room.ImageSearchDB
import com.example.imagesearch.domain.room.ImageSearchRoom
import com.example.imagesearch.domain.usecase.GeImageSearchFromStorageUseCase
import com.example.imagesearch.domain.usecase.ImageSearchRoomUseCase
import com.example.imagesearch.domain.usecase.ImageSearchUseCase
import com.example.imagesearch.domain.usecase.SaveImageSearchToStorageUseCase
import com.example.imagesearch.domain.util.CustomCoroutineDispatcherProvider
import com.example.imagesearch.ui.main.MainViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val imageSearchModule = module {

    single {
        ApiFactory.retrofit().create(ImageSearchServices::class.java)
    }
    factory {
        ImageSearchRepo(get())
    }

    single { Gson() }
    single { CustomCoroutineDispatcherProvider() }
    factory {
        ImageSearchUseCase(get(), get())
    }

    single { ImageSearchDB.getImageSearchDB(androidContext()) }
    single {
        val imageSearchDB: ImageSearchDB = get()
        imageSearchDB.imageSearchDao()
    }
    single { ImageSearchRoom(get()) }
    single { ImageSearchRoomUseCase(get()) }
    single { GeImageSearchFromStorageUseCase(get(), get()) }
    single { SaveImageSearchToStorageUseCase(get(), get()) }
    viewModel {
        MainViewModel(get(), get(), get(), get())
    }
}