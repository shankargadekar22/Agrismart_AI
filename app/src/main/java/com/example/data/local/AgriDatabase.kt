package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        CropHistoryEntity::class,
        DiseaseHistoryEntity::class,
        FertilizerHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AgriDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AgriDatabase? = null

        fun getDatabase(context: Context): AgriDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgriDatabase::class.java,
                    "smart_krushi_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
