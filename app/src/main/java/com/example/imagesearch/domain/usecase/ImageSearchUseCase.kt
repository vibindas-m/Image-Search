package com.example.imagesearch.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imagesearch.data.ImageSearchResponse
import com.example.imagesearch.domain.model.*
import com.example.imagesearch.domain.repository.ImageSearchRepo
import com.example.imagesearch.domain.util.CustomCoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class ImageSearchUseCase(
    private val imageSearchRepo: ImageSearchRepo,
    private val customCoroutineDispatcherProvider: CustomCoroutineDispatcherProvider
) :
    UseCase<String, LiveData<Result<ImageSearchResultModel>>>,
    CoroutineScope,
    Cancellable {
    var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = customCoroutineDispatcherProvider.io

    override fun execute(params: String): LiveData<Result<ImageSearchResultModel>> {
        val result = MutableLiveData<Result<ImageSearchResultModel>>()
        result.postValue(Result.Loading)
        job = launch {
            val toPost = when (val response = imageSearchRepo.getSearchImage()) {
                is Response.Success -> {
                    Result.Success(response.data.getImageSearchResult())
                }
                is Response.Error -> {
                    Result.Failure(response.errorMsg)
                }
            }
            result.postValue(toPost)
        }
        return result
    }

    override fun cancel() {
        if (coroutineContext.isActive)
            job?.cancel()
    }
}

private fun ImageSearchResponse.getImageSearchResult(): ImageSearchResultModel {
    return ImageSearchResultModel(
        totalCount = this.totalCount,
        imageSearchList = this.value?.map {
            ImageSearchModel(
                thumbnail = it.thumbnail,
                original = it.url,
                title = it.title
            )
        }
    )
}