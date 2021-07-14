package com.example.imagesearch

import android.app.Application
import com.example.imagesearch.di.imageSearchModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ImageSearchApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ImageSearchApp)
            modules(listOf(imageSearchModule))
        }
    }
}