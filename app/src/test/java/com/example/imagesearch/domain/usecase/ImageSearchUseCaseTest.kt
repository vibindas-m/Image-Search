package com.example.imagesearch.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.imagesearch.MainCoroutineRule
import com.example.imagesearch.data.ImageSearchResponse
import com.example.imagesearch.domain.model.ImageSearchResultModel
import com.example.imagesearch.domain.model.Response
import com.example.imagesearch.domain.repository.ImageSearchRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import com.example.imagesearch.domain.model.Result

class ImageSearchUseCaseTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val repo = mockk<ImageSearchRepo>()
    private lateinit var useCase: ImageSearchUseCase

    @ExperimentalCoroutinesApi
    @Before
    fun init() {
        useCase = ImageSearchUseCase(repo, provideFaveCoroutineDispatcher(testCoroutineDispatcher))
    }

    @ExperimentalCoroutinesApi
    @After
    fun after() {
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test getSearchImage usecase with success result`() = mainCoroutineRule.runBlockingTest {
        coEvery { repo.getSearchImage() } returns Response.Success(ImageSearchResponse(
            _type = "test type",
            totalCount = 25,
            value = listOf()
        ))
        testCoroutineDispatcher.pauseDispatcher()
        val resultLiveData = useCase.execute()
        val loadingResult = resultLiveData.getOrAwaitValue()
        Assert.assertTrue(loadingResult is Result.Loading)

        testCoroutineDispatcher.resumeDispatcher()
        val successResult = resultLiveData.getOrAwaitValue()
        Assert.assertTrue(successResult is Result.Success)

        Assert.assertEquals(ImageSearchResultModel(
            totalCount = 25,
            imageSearchList = listOf()
        ), (successResult as Result.Success).data
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test getSearchImage usecase with Failure result`() = mainCoroutineRule.runBlockingTest {
        coEvery { repo.getSearchImage() } returns Response.Error("Failed")
        testCoroutineDispatcher.pauseDispatcher()
        val resultLiveData = useCase.execute()
        val loadingResult = resultLiveData.getOrAwaitValue()
        Assert.assertTrue(loadingResult is Result.Loading)

        testCoroutineDispatcher.resumeDispatcher()
        val successResult = resultLiveData.getOrAwaitValue()
        Assert.assertTrue(successResult is Result.Failure)
    }

}