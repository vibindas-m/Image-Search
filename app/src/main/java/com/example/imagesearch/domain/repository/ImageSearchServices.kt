package com.example.imagesearch.domain.repository

import com.example.imagesearch.data.ImageSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageSearchServices {
    @GET("api/Search/ImageSearchAPI")
    fun getImageSearch(
        @Query("q") keyword: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("autoCorrect") autoCorrect: Boolean
    ): Call<ImageSearchResponse?>
}