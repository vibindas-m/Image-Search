package com.example.imagesearch.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.imagesearch.domain.model.Event
import com.example.imagesearch.domain.model.ImageSearchModel
import com.example.imagesearch.domain.model.ImageSearchResultModel
import com.example.imagesearch.domain.usecase.ImageSearchUseCase
import io.mockk.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.imagesearch.domain.model.Result
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
    private val gson = mockk<Gson>()

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
    fun `updateImageSearchResult test`() {
        Assert.assertEquals(null, viewModel.imageSearchList.value)
        viewModel.updateImageSearchResult(null)
        Assert.assertEquals(null, viewModel.imageSearchList.value)
        viewModel.updateImageSearchResult(
            ImageSearchResultModel(
                totalCount = 0,
                imageSearchList = listOf()
            )
        )
        Assert.assertEquals(emptyList<ImageSearchModel>(), viewModel.imageSearchList.value)

        val resultModel = ImageSearchResultModel(
            totalCount = 1, imageSearchList = listOf(
                ImageSearchModel(
                    thumbnail = "https://rapidapi.usearch.com/api/thumbnail/get?value=2635773139087957719",
                    original = "https://cdnph.upi.com/topic/ph/19660/upi/80622b9fce2df307430a5e3ff21175e3/Taylor_Swift_0.jpg",
                    title = "Taylor Swift News | Photos | Quotes | Wiki - UPI.com"
                )
            )
        )
        viewModel.updateImageSearchResult(resultModel)
        Assert.assertEquals(resultModel.imageSearchList, viewModel.imageSearchList.value)
    }


    @Test
    fun `get imageSearch test`() {
        val mockObserver = spyk(Observer<Result<ImageSearchResultModel>> {})
        val useCaseResult = MutableLiveData<Result<ImageSearchResultModel>>()
        useCaseResult.postValue(Result.Loading)
        viewModel.imageSearchEvent.observeForever(mockObserver)
        every { imageSearchUseCase.execute("test") } returns useCaseResult
        viewModel.imageSearchEventTrigger.postValue(Event("test"))
        val slot = slot<Result<ImageSearchResultModel>>()
        verify { mockObserver.onChanged(capture(slot)) }
        Assert.assertEquals(Result.Loading, slot.captured)
        verify { imageSearchUseCase.execute("test") }
    }


}