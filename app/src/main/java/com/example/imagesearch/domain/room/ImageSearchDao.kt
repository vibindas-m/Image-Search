package com.example.imagesearch.domain.room

import androidx.room.*

@Dao
interface ImageSearchDao {
    @Query("SELECT * FROM image_search_data WHERE search_key_word == :keyword")
    fun getImageSearchList(keyword: String): ImageSearchRoomData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imageSearchData: ImageSearchRoomData)

}