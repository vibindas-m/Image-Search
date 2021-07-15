package com.example.imagesearch.domain.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ImageSearchRoomData::class), version = 1)
abstract class ImageSearchDB : RoomDatabase() {
    abstract fun imageSearchDao(): ImageSearchDao

    companion object {
        fun getImageSearchDB(context: Context): ImageSearchDB {
            synchronized(this) {
                return Room.databaseBuilder(
                    context,
                    ImageSearchDB::class.java, "image_search_database"
                ).build()
            }
        }

    }
}

