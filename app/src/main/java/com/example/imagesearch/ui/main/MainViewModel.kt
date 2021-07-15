package com.example.imagesearch.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.imagesearch.domain.model.Event
import com.example.imagesearch.domain.model.ImageSearchModel
import com.example.imagesearch.domain.model.ImageSearchResultModel
import com.example.imagesearch.domain.model.Result
import com.example.imagesearch.domain.usecase.ImageSearchUseCase

internal class MainViewModel(private val imageSearchUseCase: ImageSearchUseCase) : ViewModel() {

    internal val imageSearchEventTrigger = MutableLiveData<Event<Unit>>()
    val imageSearchEvent: LiveData<Result<ImageSearchResultModel>> =
        Transformations.switchMap(imageSearchEventTrigger) {
            imageSearchUseCase.execute()
        }

    private var totalCount: Long = 0L
    private var _imageSearchList: MutableLiveData<List<ImageSearchModel>> = MutableLiveData()
    val imageSearchList: LiveData<List<ImageSearchModel>>
        get() = _imageSearchList

    private var selectedImage: ImageSearchModel? = null

    fun updateImageSearchResult(data: ImageSearchResultModel?) {
        totalCount = data?.totalCount ?: 0
        _imageSearchList.value = data?.imageSearchList
    }

    fun updateSelectedImage(data: ImageSearchModel) {
        selectedImage = data
    }

    fun getSelectedImage(): String {
        return selectedImage?.original ?: ""
    }

}