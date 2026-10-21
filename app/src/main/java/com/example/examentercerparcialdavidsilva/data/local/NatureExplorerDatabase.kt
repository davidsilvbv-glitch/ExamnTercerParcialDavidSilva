package com.example.examentercerparcialdavidsilva.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        NatureWeatherDataEntity::class,
        FaunaFactDataEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NatureExplorerDatabase : RoomDatabase() {

    abstract fun natureExplorerDao(): NatureExplorerDao

    companion object {
        @kotlin.jvm.Volatile
        private var INSTANCE: NatureExplorerDatabase? = null

        fun getInstance(context: Context): NatureExplorerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NatureExplorerDatabase::class.java,
                    "nature_explorer_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
