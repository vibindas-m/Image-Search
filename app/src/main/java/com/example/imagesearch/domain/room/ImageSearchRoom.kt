package com.example.imagesearch.domain.room

import androidx.annotation.WorkerThread

class ImageSearchRoom constructor(private val imageSearchDao: ImageSearchDao) {
    @WorkerThread
    fun insert(imageSearchData: ImageSearchRoomData) {
        imageSearchDao.insert(imageSearchData)
    }
    @WorkerThread
    fun getImageSearchList(keyword: String): ImageSearchRoomData? {
        return imageSearchDao.getImageSearchList(keyword)
    }

}