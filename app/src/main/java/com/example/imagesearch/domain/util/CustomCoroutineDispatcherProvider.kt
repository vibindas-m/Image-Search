package com.example.imagesearch.domain.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal open class CustomCoroutineDispatcherProvider {
    open val main: CoroutineDispatcher by lazy {
        Dispatchers.Main
    }
    open val io: CoroutineDispatcher by lazy {
        Dispatchers.IO
    }
}