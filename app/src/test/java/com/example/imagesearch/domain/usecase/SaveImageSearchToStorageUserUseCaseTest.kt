package com.example.imagesearch.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.imagesearch.MainCoroutineRule
import com.example.imagesearch.domain.model.ImageSearchModel
import com.example.imagesearch.domain.model.Result
import com.example.imagesearch.domain.room.ImageSearchRoomData
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*

class SaveImageSearchToStorageUserUseCaseTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val imageSearchUseCase = mockk<ImageSearchRoomUseCase>()
    private lateinit var useCase: SaveImageSearchToStorageUseCase
    private lateinit var imageSearchListString: String

    @ExperimentalCoroutinesApi
    @Before
    fun init() {
        useCase = SaveImageSearchToStorageUseCase(imageSearchUseCase, provideFaveCoroutineDispatcher(testCoroutineDispatcher))
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

        imageSearchListString = Gson().toJson(doubleList).toString()
    }

    @ExperimentalCoroutinesApi
    @After
    fun after() {
        testCoroutineDispatcher.cleanupTestCoroutines()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `test SaveImageSearchToStorageUseCase request params`() = mainCoroutineRule.runBlockingTest {
        val slot = slot<ImageSearchRoomData>()
        coEvery { imageSearchUseCase.saveImageSearchData(capture(slot))} returns Unit


        //when
        useCase.execute(ImageSearchRoomData("hello", true, imageSearchListString))
        testCoroutineDispatcher.resumeDispatcher()

        //Then
        Assert.assertEquals(ImageSearchRoomData("hello", true, imageSearchListString), slot.captured)
    }
    @ExperimentalCoroutinesApi
    @Test
    fun `SaveImageSearchToStorageUseCase with success result`() = mainCoroutineRule.runBlockingTest {
        coEvery { imageSearchUseCase.saveImageSearchData(ImageSearchRoomData("hello", true, imageSearchListString))} returns Unit
        testCoroutineDispatcher.pauseDispatcher()
        val resultLiveData = useCase.execute(ImageSearchRoomData("hello", true, imageSearchListString))
        val loadingResult = resultLiveData.getOrAwaitValue()

        Assert.assertTrue(loadingResult.getContentIfNotHandled() is Result.Loading)

        testCoroutineDispatcher.resumeDispatcher()
        val successResult = resultLiveData.getOrAwaitValue()
        Assert.assertTrue(successResult.getContentIfNotHandled() is Result.Success)

    }
}