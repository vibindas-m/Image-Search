package com.example.imagesearch.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imagesearch.domain.model.*
import com.example.imagesearch.domain.room.ImageSearchRoomData
import com.example.imagesearch.domain.util.CustomCoroutineDispatcherProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import java.lang.Exception

internal class SaveImageSearchToStorageUseCase(private val imageSearchRoomUseCase: ImageSearchRoomUseCase,
                                               private val customCoroutineDispatcherProvider: CustomCoroutineDispatcherProvider
) :
    UseCase<ImageSearchRoomData, LiveData<Event<Result<Boolean>>>>,
    CoroutineScope,
    Cancellable {
    var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = customCoroutineDispatcherProvider.io

    override fun execute(params: ImageSearchRoomData): LiveData<Event<Result<Boolean>>> {
        val result = MutableLiveData<Event<Result<Boolean>>>()
        result.postValue(Event(Result.Loading))
        job = launch {
            val toPost = try {
                imageSearchRoomUseCase.saveImageSearchData(params)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Failure("Data not Saved")
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