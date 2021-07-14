package com.example.imagesearch.domain.repository

import com.example.imagesearch.data.ImageSearchResponse
import retrofit2.Call
import retrofit2.http.GET

interface ImageSearchServices {
    @GET("api/Search/ImageSearchAPI?q=taylor%20swift&pageNumber=1&pageSize=10&autoCorrect=true")
    fun getImageSearch(): Call<ImageSearchResponse?>
}