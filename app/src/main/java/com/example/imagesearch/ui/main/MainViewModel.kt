package com.example.imagesearch.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.imagesearch.domain.model.Event
import com.example.imagesearch.domain.model.ImageSearchModel
import com.example.imagesearch.domain.model.ImageSearchResultModel
import com.example.imagesearch.domain.model.Result
import com.example.imagesearch.domain.room.ImageSearchRoomData
import com.example.imagesearch.domain.usecase.GeImageSearchFromStorageUseCase
import com.example.imagesearch.domain.usecase.ImageSearchUseCase
import com.example.imagesearch.domain.usecase.SaveImageSearchToStorageUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal class MainViewModel(
    private val imageSearchUseCase: ImageSearchUseCase,
    private val getImageSearchToStorageUseCase: GeImageSearchFromStorageUseCase,
    private val saveImageSearchToStorageUseCase: SaveImageSearchToStorageUseCase,
    private val gson: Gson
) : ViewModel() {

    internal val imageSearchEventTrigger = MutableLiveData<Event<String>>()
    val imageSearchEvent: LiveData<Result<ImageSearchResultModel>> =
        Transformations.switchMap(imageSearchEventTrigger) {
            it.getContentIfNotHandled()?.let { keyword ->
                imageSearchUseCase.execute(keyword)
            }
        }


    internal val getImageSearchFromStorageTrigger = MutableLiveData<Event<String>>()
    val getImageSearchFromStorageEvent: LiveData<Result<ImageSearchRoomData>> =
        Transformations.switchMap(getImageSearchFromStorageTrigger) {
            it.getContentIfNotHandled()?.let { keyword ->
                getImageSearchToStorageUseCase.execute(keyword)
            }
        }

    internal val saveImageSearchDataStorageTrigger = MutableLiveData<Event<ImageSearchRoomData>>()
    val saveImageSearchDataStorageEvent: LiveData<Result<Boolean>> =
        Transformations.switchMap(saveImageSearchDataStorageTrigger) {
            it.getContentIfNotHandled()?.let { imageData ->
                saveImageSearchToStorageUseCase.execute(imageData)
            }
        }

    private var _imageSearchRoomData: MutableLiveData<ImageSearchRoomData> = MutableLiveData()
    val imageSearchRoomData: LiveData<ImageSearchRoomData>
        get() = _imageSearchRoomData

    private var _imageSearchList: MutableLiveData<List<ImageSearchModel>> = MutableLiveData()
    val imageSearchList: LiveData<List<ImageSearchModel>>
        get() = _imageSearchList

    private var selectedImage: ImageSearchModel? = null

    fun updateImageSearchResult(data: ImageSearchResultModel?) {
        data?.let {
            _imageSearchRoomData.value =
                ImageSearchRoomData("", it.totalCount ?: 0, gson.toJson(it.imageSearchList))
        }
    }

    fun updateSelectedImage(data: ImageSearchModel) {
        selectedImage = data
    }

    fun getSelectedImage(): String {
        return selectedImage?.original ?: ""
    }

    fun updateImageSearchFromStorage(data: ImageSearchRoomData?) {
        data?.let {
            _imageSearchRoomData.value = data
            _imageSearchList.value = gson.fromJson(data.searchList, Array<ImageSearchModel>::class.java).toList()
        }

    }


    fun getImageSearchRoomData(data: ImageSearchResultModel?): ImageSearchRoomData? {
        _imageSearchList.value = data?.imageSearchList
        data?.let {
            _imageSearchRoomData.value =
                ImageSearchRoomData("", it.totalCount ?: 0, gson.toJson(it.imageSearchList))
        }
        return imageSearchRoomData.value
    }

}