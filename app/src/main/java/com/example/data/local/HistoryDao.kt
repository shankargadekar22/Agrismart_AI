package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM crop_history ORDER BY timestamp DESC LIMIT 20")
    fun getCropHistoryFlow(): Flow<List<CropHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCropHistory(entry: CropHistoryEntity)

    @Query("SELECT * FROM disease_history ORDER BY timestamp DESC LIMIT 20")
    fun getDiseaseHistoryFlow(): Flow<List<DiseaseHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiseaseHistory(entry: DiseaseHistoryEntity)

    @Query("SELECT * FROM fertilizer_history ORDER BY timestamp DESC LIMIT 20")
    fun getFertilizerHistoryFlow(): Flow<List<FertilizerHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFertilizerHistory(entry: FertilizerHistoryEntity)
}
