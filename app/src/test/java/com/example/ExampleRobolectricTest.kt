package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AgriDatabase
import com.example.data.local.UserEntity
import com.example.data.ml.CropRecommendationEngine
import com.example.data.ml.DiseaseDetectionEngine
import com.example.data.ml.FertilizerCalculator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun testAppNameString() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Smart Krushi AI", appName)
    }

    @Test
    fun testCropRecommendationML_Rice() {
        // Typical alluvial rice condition: N:80, P:48, K:40, Temp:24, Hum:82, pH:6.5, Rain:230
        val result = CropRecommendationEngine.recommend(
            n = 80f, p = 48f, k = 40f,
            temp = 24f, humidity = 82f, ph = 6.5f, rainfall = 230f
        )
        assertNotNull(result)
        assertEquals("Rice (Paddy)", result.topCrop.name)
        assertTrue("Confidence should be high (>70%)", result.confidencePercent >= 70)
        assertTrue("Should have alternate crops", result.alternateCrops.isNotEmpty())
    }

    @Test
    fun testCropRecommendationML_Chickpea() {
        // High P & K, moderate N, cool temp: Chickpea
        val result = CropRecommendationEngine.recommend(
            n = 40f, p = 68f, k = 80f,
            temp = 19f, humidity = 17f, ph = 7.3f, rainfall = 80f
        )
        assertNotNull(result)
        assertEquals("Chickpea (Gram)", result.topCrop.name)
        assertTrue(result.confidencePercent >= 70)
    }

    @Test
    fun testFertilizerCalculator() {
        val result = FertilizerCalculator.calculate(
            cropName = "Rice (Paddy)",
            soilN = 40f,
            soilP = 20f,
            soilK = 20f,
            acres = 2f
        )
        assertNotNull(result)
        assertEquals(2f, result.farmAreaAcres)
        assertTrue("Urea bags should be positive", result.ureaBags > 0)
        assertTrue("DAP bags should be positive", result.dapBags > 0)
        assertTrue("Should have 3 split stages", result.splitSchedule.size == 3)
    }

    @Test
    fun testDiseaseDetectionPreset() {
        val diag = DiseaseDetectionEngine.getDiagnosisForPreset("Tomato Early Blight")
        assertNotNull(diag)
        assertTrue(diag.diseaseName.contains("Early Blight"))
        assertEquals("Alternaria solani (Fungal)", diag.pathogen)
        assertTrue("Should have organic treatment", diag.organicTreatment.isNotBlank())
        assertTrue("Should have chemical treatment", diag.chemicalTreatment.isNotBlank())
    }

    @Test
    fun testRoomDatabaseUserPersistence() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AgriDatabase.getDatabase(context)
        val userDao = db.userDao()

        userDao.clearUsers()
        val testFarmer = UserEntity(
            name = "Suresh Shinde",
            phone = "9822001122",
            village = "Baramati",
            farmSizeAcres = 6.0f,
            soilType = "Black Cotton",
            irrigationType = "Drip",
            pinHash = "4321"
        )
        userDao.insertUser(testFarmer)

        val fetched = userDao.getActiveUser()
        assertNotNull(fetched)
        assertEquals("Suresh Shinde", fetched?.name)
        assertEquals("9822001122", fetched?.phone)
        assertEquals(6.0f, fetched?.farmSizeAcres)
    }
}
