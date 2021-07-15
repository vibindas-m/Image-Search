package com.example.imagesearch.domain.usecase

import com.example.imagesearch.domain.room.ImageSearchRoom
import com.example.imagesearch.domain.room.ImageSearchRoomData

class ImageSearchRoomUseCase(
    private val imageSearchRoom: ImageSearchRoom
) {
    fun getImageSearchData(keyword: String): ImageSearchRoomData? {
        return imageSearchRoom.getImageSearchList(keyword)
    }

    fun saveImageSearchData(
        imageSearchData: ImageSearchRoomData
    ) {
        imageSearchRoom.insert(imageSearchData)
    }

}