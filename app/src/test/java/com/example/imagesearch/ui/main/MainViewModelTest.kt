package com.example.imagesearch.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.imagesearch.data.ImageSearchRequestData
import com.example.imagesearch.domain.model.*
import com.example.imagesearch.domain.usecase.ImageSearchUseCase
import io.mockk.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.imagesearch.domain.room.ImageSearchRoomData
import com.example.imagesearch.domain.usecase.GeImageSearchFromStorageUseCase
import com.example.imagesearch.domain.usecase.SaveImageSearchToStorageUseCase
import com.google.gson.Gson

class MainViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private val imageSearchUseCase = mockk<ImageSearchUseCase>()
    private val getImageSearchToStorageUseCase = mockk<GeImageSearchFromStorageUseCase>()
    private val saveImageSearchToStorageUseCase = mockk<SaveImageSearchToStorageUseCase>()
    private val gson = Gson()

    @Before
    fun setUp() {
        viewModel = MainViewModel(
            imageSearchUseCase,
            getImageSearchToStorageUseCase,
            saveImageSearchToStorageUseCase,
            gson
        )
    }

    @Test
    fun `updateSelectedImage Test`() {
        Assert.assertEquals(null, viewModel.getSelectedImage())
        val imageSearchModel = ImageSearchModel(
            thumbnail = "https://rapidapi.usearch.com/api/thumbnail/get?value=8421264495872280999",
            original = "https://cdn.cnn.com/cnnnext/dam/assets/201215085238-taylor-swift-2020-sundance-super-tease.jpg",
            title = "Taylor Swift's re-recorded 'Love Story' is back on top of the charts - CNN"
        )
        viewModel.updateSelectedImage(imageSearchModel)
        Assert.assertEquals(imageSearchModel, viewModel.getSelectedImage())
    }

    @Test
    fun `updateImageSearchFromStorage Test`() {
        Assert.assertEquals(null, viewModel.imageSearchList.value)
        Assert.assertEquals(null, viewModel.imageSearchRoomData.value)
        viewModel.updateImageSearchFromStorage(null)
        Assert.assertEquals(null, viewModel.imageSearchList.value)
        Assert.assertEquals(null, viewModel.imageSearchRoomData.value)
        viewModel.updateImageSearchFromStorage(ImageSearchRoomData("hello", true, null))
        Assert.assertEquals(null, viewModel.imageSearchList.value)
        Assert.assertEquals(
            ImageSearchRoomData("hello", true, null),
            viewModel.imageSearchRoomData.value
        )
        val list = listOf<ImageSearchModel>(
            ImageSearchModel(
                thumbnail = "https://rapidapi.usearch.com/api/thumbnail/get?value=8421264495872280999",
                original = "https://cdn.cnn.com/cnnnext/dam/assets/201215085238-taylor-swift-2020-sundance-super-tease.jpg",
                title = "Taylor Swift's re-recorded 'Love Story' is back on top of the charts - CNN"
            )
        )
        val listtoString = gson.toJson(list).toString()
        viewModel.updateImageSearchFromStorage(ImageSearchRoomData("hello", false, listtoString))
        Assert.assertEquals(list, viewModel.imageSearchList.value)
        Assert.assertEquals(
            ImageSearchRoomData("hello", false, listtoString),
            viewModel.imageSearchRoomData.value
        )
        viewModel.resetFetchedList()
        Assert.assertEquals(null, viewModel.imageSearchList.value)
    }

    @Test
    fun `getHasNext Test`() {
        Assert.assertEquals(false, viewModel.getHasNext())
        viewModel.updateImageSearchFromStorage(ImageSearchRoomData("hello", true, null))
        Assert.assertEquals(true, viewModel.getHasNext())
        viewModel.updateImageSearchFromStorage(ImageSearchRoomData("hello", false, null))
        Assert.assertEquals(false, viewModel.getHasNext())
    }

    @Test
    fun `getAndUpdateImageSearchRoomData Test`() {
        Assert.assertEquals(null, viewModel.imageSearchRoomData.value)
        Assert.assertEquals(null, viewModel.getAndUpdateImageSearchRoomData(null, "hello"))

        Assert.assertEquals(ImageSearchRoomData("hello", true, "[]"), viewModel.getAndUpdateImageSearchRoomData(ImageSearchResultModel(
            paginationModel = PaginationModel(true,25, 2),
            imageSearchList = listOf()
        ), "hello"))

        Assert.assertEquals(ImageSearchRoomData("hello", true, "[]"), viewModel.imageSearchRoomData.value)

        val list = listOf<ImageSearchModel>(
            ImageSearchModel(
                thumbnail = "https://rapidapi.usearch.com/api/thumbnail/get?value=8421264495872280999",
                original = "https://cdn.cnn.com/cnnnext/dam/assets/201215085238-taylor-swift-2020-sundance-super-tease.jpg",
                title = "Taylor Swift's re-recorded 'Love Story' is back on top of the charts - CNN"
            )
        )
        val listtoString = gson.toJson(list).toString()
        Assert.assertEquals(ImageSearchRoomData("hello", true, listtoString), viewModel.getAndUpdateImageSearchRoomData(ImageSearchResultModel(
            paginationModel = PaginationModel(true,25, 2),
            imageSearchList = list
        ), "hello"))
        Assert.assertEquals(ImageSearchRoomData("hello", true, listtoString), viewModel.imageSearchRoomData.value)

        Assert.assertEquals(list, viewModel.imageSearchList.value)
        val doubleList = listOf<ImageSearchModel>(
            ImageSearchModel(
                thumbnail = "https://rapidapi.usearch.com/api/thumbnail/get?value=8421264495872280999",
                original = "https://cdn.cnn.com/cnnnext/dam/assets/201215085238-taylor-swift-2020-sundance-super-tease.jpg",
                title = "Taylor Swift's re-recorded 'Love Story' is back on top of the charts - CNN"
            ),
            ImageSearchModel(
                thumbnail = "https://rapidapi.usearch.com/api/thumbnail/get?value=8421264495872280999",
                original = "https://cdn.cnn.com/cnnnext/dam/assets/201215085238-taylor-swift-2020-sundance-super-tease.jpg",
                title = "Taylor Swift's re-recorded 'Love Story' is back on top of the charts - CNN"
            )
        )
        Assert.assertEquals(ImageSearchRoomData("hello", true, listtoString), viewModel.getAndUpdateImageSearchRoomData(ImageSearchResultModel(
            paginationModel = PaginationModel(true,25, 2),
            imageSearchList = list
        ), "hello"))
        Assert.assertEquals(ImageSearchRoomData("hello", true, listtoString), viewModel.imageSearchRoomData.value)

        Assert.assertEquals(doubleList, viewModel.imageSearchList.value)
        viewModel.resetFetchedList()
        Assert.assertEquals(null, viewModel.imageSearchList.value)
    }

}