package com.example.imagesearch.domain.repository


import com.example.imagesearch.data.ImageSearchResponse
import com.example.imagesearch.domain.model.Response
import retrofit2.await

class ImageSearchRepo(private val imageSearchServices: ImageSearchServices) {

    suspend fun getSearchImage(): Response<ImageSearchResponse> {
        return try {
            val result = imageSearchServices.getImageSearch().await()
            with(result) {
                if (this != null) {
                    Response.Success(this)
                } else {
                    Response.Error("No Data")
                }
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "")
        }
    }
}