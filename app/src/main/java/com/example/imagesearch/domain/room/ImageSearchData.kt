package com.example.imagesearch.domain.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.imagesearch.domain.model.ImageSearchModel

@Entity(tableName = "image_search_data")
data class ImageSearchRoomData(
    @PrimaryKey
    @ColumnInfo(name = "search_key_word") val searchKeyword: String,
    @ColumnInfo(name = "total_count") val totalCount: Long?,
    @ColumnInfo(name = "search_list") val searchList: String?
)

