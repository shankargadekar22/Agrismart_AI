package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val village: String,
    val farmSizeAcres: Float,
    val soilType: String,
    val irrigationType: String,
    val pinHash: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "crop_history")
data class CropHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val nitrogen: Float,
    val phosphorus: Float,
    val potassium: Float,
    val temperature: Float,
    val humidity: Float,
    val ph: Float,
    val rainfall: Float,
    val recommendedCrop: String,
    val confidence: Float,
    val season: String
)

@Entity(tableName = "disease_history")
data class DiseaseHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val cropName: String,
    val diseaseName: String,
    val pathogen: String,
    val severity: String,
    val confidence: Float,
    val remedySummary: String
)

@Entity(tableName = "fertilizer_history")
data class FertilizerHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val cropName: String,
    val ureaBags: Float,
    val dapBags: Float,
    val mopBags: Float,
    val organicManureTonnes: Float
)
