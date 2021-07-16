package com.example.imagesearch.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imagesearch.domain.model.*
import com.example.imagesearch.domain.room.ImageSearchRoomData
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import com.example.imagesearch.domain.util.CustomCoroutineDispatcherProvider

internal class GeImageSearchFromStorageUseCase(private val imageSearchRoomUseCase: ImageSearchRoomUseCase,
                                      private val customCoroutineDispatcherProvider: CustomCoroutineDispatcherProvider
) : UseCase<String, LiveData<Event<Result<ImageSearchRoomData>>>>,
    CoroutineScope,
    Cancellable {
    var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = customCoroutineDispatcherProvider.io


    override fun execute(params: String): LiveData<Event<Result<ImageSearchRoomData>>> {
        val result = MutableLiveData<Event<Result<ImageSearchRoomData>>>()
        result.postValue(Event(Result.Loading))
        job = launch {
            val toPost = when (val response = imageSearchRoomUseCase.getImageSearchData(params)) {
                null -> Result.Failure("No data, Plz connect your internet and try again")
                else -> Result.Success(response)
            }
            result.postValue(Event(toPost))
        }
        return result
    }

    override fun cancel() {
        if (coroutineContext.isActive)
            job?.cancel()
    }
}