package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PlayerProfileEntity::class,
        UnlockedCardEntity::class,
        StageProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GanjnamehDatabase : RoomDatabase() {
    abstract fun dao(): GanjnamehDao

    companion object {
        @Volatile
        private var INSTANCE: GanjnamehDatabase? = null

        fun getInstance(context: Context): GanjnamehDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GanjnamehDatabase::class.java,
                    "ganjnameh_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
