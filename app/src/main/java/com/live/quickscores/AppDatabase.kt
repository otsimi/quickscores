package com.live.quickscores

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DataClassFavorite::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "matches_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE=instance
                instance

            }
        }
    }

}