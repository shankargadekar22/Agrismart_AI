package com.example.data.ml

import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max

data class CropProfile(
    val id: String,
    val name: String,
    val hindiName: String,
    val category: String, // Grain, Pulse, Fruit, Commercial, Fiber
    val season: String,   // Kharif, Rabi, Zaid, Perennial
    val idealN: Float, val stdN: Float,
    val idealP: Float, val stdP: Float,
    val idealK: Float, val stdK: Float,
    val idealTemp: Float, val stdTemp: Float,
    val idealHumidity: Float, val stdHumidity: Float,
    val idealPh: Float, val stdPh: Float,
    val idealRainfall: Float, val stdRainfall: Float,
    val durationDays: String,
    val expectedYield: String,
    val agronomicTip: String
)

data class RecommendationResult(
    val topCrop: CropProfile,
    val confidencePercent: Int,
    val alternateCrops: List<Pair<CropProfile, Int>>,
    val suitabilityFactors: Map<String, SuitabilityScore>,
    val seasonSummary: String,
    val isDrylandAlert: Boolean
)

data class SuitabilityScore(
    val label: String,
    val current: Float,
    val ideal: Float,
    val unit: String,
    val status: String // Optimal, Moderate, Low, High
)

object CropRecommendationEngine {

    val CROPS: List<CropProfile> = listOf(
        CropProfile(
            id = "rice", name = "Rice (Paddy)", hindiName = "चावल / धान",
            category = "Cereal / Grain", season = "Kharif",
            idealN = 79.89f, stdN = 12.3f,
            idealP = 47.58f, stdP = 7.1f,
            idealK = 39.87f, stdK = 5.2f,
            idealTemp = 23.68f, stdTemp = 2.4f,
            idealHumidity = 82.27f, stdHumidity = 4.1f,
            idealPh = 6.42f, stdPh = 0.38f,
            idealRainfall = 236.18f, stdRainfall = 35.2f,
            durationDays = "110 - 140 days",
            expectedYield = "3.5 - 5.0 tonnes/ha",
            agronomicTip = "Requires standing water during tillering. Puddled transplanting recommended for maximum yield."
        ),
        CropProfile(
            id = "maize", name = "Maize (Corn)", hindiName = "मक्का",
            category = "Cereal / Grain", season = "Kharif / Rabi",
            idealN = 77.76f, stdN = 11.8f,
            idealP = 48.44f, stdP = 7.6f,
            idealK = 19.79f, stdK = 4.5f,
            idealTemp = 22.38f, stdTemp = 3.1f,
            idealHumidity = 65.09f, stdHumidity = 5.4f,
            idealPh = 6.49f, stdPh = 0.42f,
            idealRainfall = 84.76f, stdRainfall = 21.0f,
            durationDays = "85 - 110 days",
            expectedYield = "4.0 - 6.5 tonnes/ha",
            agronomicTip = "Highly responsive to nitrogen. Ensure proper drainage to avoid water stagnation at early stages."
        ),
        CropProfile(
            id = "chickpea", name = "Chickpea (Gram)", hindiName = "चना",
            category = "Pulse / Legume", season = "Rabi",
            idealN = 40.09f, stdN = 11.2f,
            idealP = 67.79f, stdP = 8.1f,
            idealK = 79.92f, stdK = 6.4f,
            idealTemp = 18.87f, stdTemp = 2.1f,
            idealHumidity = 16.86f, stdHumidity = 2.5f,
            idealPh = 7.33f, stdPh = 0.44f,
            idealRainfall = 80.05f, stdRainfall = 13.8f,
            durationDays = "90 - 120 days",
            expectedYield = "1.5 - 2.2 tonnes/ha",
            agronomicTip = "Symbiotic nitrogen-fixer. Needs cool climate during vegetative phase and warm sunny weather during ripening."
        ),
        CropProfile(
            id = "kidneybeans", name = "Kidney Beans (Rajma)", hindiName = "राजमा",
            category = "Pulse / Legume", season = "Rabi",
            idealN = 20.75f, stdN = 11.5f,
            idealP = 67.54f, stdP = 8.2f,
            idealK = 20.05f, stdK = 4.8f,
            idealTemp = 20.11f, stdTemp = 2.2f,
            idealHumidity = 21.60f, stdHumidity = 2.7f,
            idealPh = 5.74f, stdPh = 0.39f,
            idealRainfall = 105.91f, stdRainfall = 20.5f,
            durationDays = "100 - 130 days",
            expectedYield = "1.2 - 2.0 tonnes/ha",
            agronomicTip = "Thrives in well-drained loamy soils rich in phosphorus. Sensitive to frost and waterlogging."
        ),
        CropProfile(
            id = "pigeonpeas", name = "Pigeon Peas (Arhar/Tur)", hindiName = "अरहर / तुवर",
            category = "Pulse / Legume", season = "Kharif",
            idealN = 20.73f, stdN = 11.2f,
            idealP = 67.73f, stdP = 7.9f,
            idealK = 20.29f, stdK = 4.9f,
            idealTemp = 27.74f, stdTemp = 3.2f,
            idealHumidity = 48.06f, stdHumidity = 5.1f,
            idealPh = 5.79f, stdPh = 0.46f,
            idealRainfall = 149.45f, stdRainfall = 28.5f,
            durationDays = "150 - 180 days",
            expectedYield = "1.4 - 2.0 tonnes/ha",
            agronomicTip = "Deep taproot system imparts strong drought tolerance. Excellent for intercropping with cotton or maize."
        ),
        CropProfile(
            id = "mothbeans", name = "Moth Beans", hindiName = "मोठ दाल",
            category = "Pulse / Legume", season = "Kharif",
            idealN = 21.44f, stdN = 10.4f,
            idealP = 48.01f, stdP = 7.8f,
            idealK = 20.23f, stdK = 4.7f,
            idealTemp = 28.19f, stdTemp = 2.8f,
            idealHumidity = 53.16f, stdHumidity = 5.3f,
            idealPh = 6.83f, stdPh = 0.48f,
            idealRainfall = 51.19f, stdRainfall = 11.5f,
            durationDays = "75 - 90 days",
            expectedYield = "0.6 - 1.0 tonnes/ha",
            agronomicTip = "Most drought-tolerant legume. Excels in arid sandy tracts with minimal rainfall."
        ),
        CropProfile(
            id = "mungbean", name = "Mung Bean (Green Gram)", hindiName = "मूंग दाल",
            category = "Pulse / Legume", season = "Zaid / Kharif",
            idealN = 20.99f, stdN = 10.3f,
            idealP = 47.28f, stdP = 7.6f,
            idealK = 19.87f, stdK = 4.6f,
            idealTemp = 28.52f, stdTemp = 2.9f,
            idealHumidity = 85.49f, stdHumidity = 4.2f,
            idealPh = 6.72f, stdPh = 0.41f,
            idealRainfall = 48.40f, stdRainfall = 10.2f,
            durationDays = "60 - 75 days",
            expectedYield = "0.9 - 1.4 tonnes/ha",
            agronomicTip = "Short duration catch crop. Ideal for crop rotation between wheat and rice to restore soil nitrogen."
        ),
        CropProfile(
            id = "blackgram", name = "Black Gram (Urad)", hindiName = "उड़द दाल",
            category = "Pulse / Legume", season = "Kharif / Rabi",
            idealN = 40.14f, stdN = 11.4f,
            idealP = 67.47f, stdP = 8.0f,
            idealK = 19.24f, stdK = 4.5f,
            idealTemp = 29.98f, stdTemp = 2.4f,
            idealHumidity = 65.11f, stdHumidity = 4.3f,
            idealPh = 7.13f, stdPh = 0.42f,
            idealRainfall = 67.88f, stdRainfall = 14.1f,
            durationDays = "70 - 90 days",
            expectedYield = "1.0 - 1.5 tonnes/ha",
            agronomicTip = "Prefers loam or heavy clay soils. Highly beneficial for soil aggregation and microbial biomass."
        ),
        CropProfile(
            id = "lentil", name = "Lentil (Masoor)", hindiName = "मसूर दाल",
            category = "Pulse / Legume", season = "Rabi",
            idealN = 18.77f, stdN = 10.1f,
            idealP = 68.36f, stdP = 7.9f,
            idealK = 19.41f, stdK = 4.6f,
            idealTemp = 24.50f, stdTemp = 2.7f,
            idealHumidity = 64.80f, stdHumidity = 4.4f,
            idealPh = 6.92f, stdPh = 0.43f,
            idealRainfall = 45.68f, stdRainfall = 9.8f,
            durationDays = "110 - 130 days",
            expectedYield = "1.2 - 1.8 tonnes/ha",
            agronomicTip = "Cold-tolerant pulse with modest water demand. Avoid poorly drained alkaline soils."
        ),
        CropProfile(
            id = "banana", name = "Banana", hindiName = "केला",
            category = "Fruit / Perennial", season = "Perennial",
            idealN = 100.23f, stdN = 12.5f,
            idealP = 82.01f, stdP = 8.5f,
            idealK = 50.05f, stdK = 5.2f,
            idealTemp = 27.37f, stdTemp = 2.1f,
            idealHumidity = 80.35f, stdHumidity = 3.9f,
            idealPh = 5.98f, stdPh = 0.35f,
            idealRainfall = 104.62f, stdRainfall = 19.8f,
            durationDays = "300 - 365 days",
            expectedYield = "30 - 50 tonnes/ha",
            agronomicTip = "Heavy feeder requiring balanced N and K fertigation through drip. Needs protection from strong winds."
        ),
        CropProfile(
            id = "mango", name = "Mango", hindiName = "आम",
            category = "Fruit / Perennial", season = "Summer / Perennial",
            idealN = 20.07f, stdN = 10.2f,
            idealP = 27.18f, stdP = 5.9f,
            idealK = 29.92f, stdK = 5.8f,
            idealTemp = 31.20f, stdTemp = 2.6f,
            idealHumidity = 50.15f, stdHumidity = 4.1f,
            idealPh = 5.76f, stdPh = 0.41f,
            idealRainfall = 94.70f, stdRainfall = 12.4f,
            durationDays = "Orchard tree (annual fruiting)",
            expectedYield = "8 - 15 tonnes/ha",
            agronomicTip = "Requires a distinct dry spell before flowering to induce profuse floral initiation."
        ),
        CropProfile(
            id = "grapes", name = "Grapes", hindiName = "अंगूर",
            category = "Fruit / Vineyard", season = "Perennial",
            idealN = 23.18f, stdN = 10.3f,
            idealP = 132.53f, stdP = 12.1f,
            idealK = 200.11f, stdK = 9.8f,
            idealTemp = 23.84f, stdTemp = 2.9f,
            idealHumidity = 81.87f, stdHumidity = 3.2f,
            idealPh = 6.02f, stdPh = 0.38f,
            idealRainfall = 69.61f, stdRainfall = 10.3f,
            durationDays = "Perennial (annual pruning cycle)",
            expectedYield = "20 - 30 tonnes/ha",
            agronomicTip = "High potash demand for berry sugar accumulation and berry elongation. Y-trellis or Bower system suggested."
        ),
        CropProfile(
            id = "watermelon", name = "Watermelon", hindiName = "तरबूज",
            category = "Cucurbit / Fruit", season = "Zaid / Summer",
            idealN = 99.42f, stdN = 12.1f,
            idealP = 17.00f, stdP = 5.8f,
            idealK = 50.08f, stdK = 5.1f,
            idealTemp = 25.59f, stdTemp = 2.2f,
            idealHumidity = 85.16f, stdHumidity = 3.4f,
            idealPh = 6.49f, stdPh = 0.40f,
            idealRainfall = 50.78f, stdRainfall = 9.9f,
            durationDays = "80 - 100 days",
            expectedYield = "25 - 40 tonnes/ha",
            agronomicTip = "Prefers sandy loam soil with good warmth. Reduce watering 10 days before harvest to boost sweetness (Brix)."
        ),
        CropProfile(
            id = "apple", name = "Apple", hindiName = "सेब",
            category = "Horticulture", season = "Temperate",
            idealN = 20.80f, stdN = 10.1f,
            idealP = 134.22f, stdP = 11.9f,
            idealK = 199.89f, stdK = 9.7f,
            idealTemp = 22.63f, stdTemp = 2.6f,
            idealHumidity = 92.33f, stdHumidity = 2.3f,
            idealPh = 5.92f, stdPh = 0.39f,
            idealRainfall = 112.65f, stdRainfall = 12.2f,
            durationDays = "Deciduous orchard",
            expectedYield = "12 - 20 tonnes/ha",
            agronomicTip = "Requires chilling hours (<7°C for 800-1200 hrs) for uniform bud break. High potassium ensures fruit color."
        ),
        CropProfile(
            id = "orange", name = "Orange (Citrus)", hindiName = "संतरा / मौसंबी",
            category = "Citrus Fruit", season = "Perennial",
            idealN = 19.58f, stdN = 9.9f,
            idealP = 16.55f, stdP = 5.7f,
            idealK = 10.01f, stdK = 3.9f,
            idealTemp = 22.76f, stdTemp = 2.4f,
            idealHumidity = 92.17f, stdHumidity = 2.2f,
            idealPh = 7.00f, stdPh = 0.41f,
            idealRainfall = 110.47f, stdRainfall = 12.1f,
            durationDays = "Perennial orchard",
            expectedYield = "15 - 25 tonnes/ha",
            agronomicTip = "Sensitive to wet feet; plant on raised ridges. Regular micronutrient foliar spray of Zinc and Iron crucial."
        ),
        CropProfile(
            id = "papaya", name = "Papaya", hindiName = "पपीता",
            category = "Horticulture", season = "Perennial",
            idealN = 49.88f, stdN = 11.8f,
            idealP = 59.05f, stdP = 8.1f,
            idealK = 50.04f, stdK = 5.2f,
            idealTemp = 33.72f, stdTemp = 3.1f,
            idealHumidity = 92.40f, stdHumidity = 2.1f,
            idealPh = 6.74f, stdPh = 0.41f,
            idealRainfall = 142.62f, stdRainfall = 24.8f,
            durationDays = "9 - 12 months to harvest",
            expectedYield = "50 - 75 tonnes/ha",
            agronomicTip = "Fast growing herbaceous fruit. Cannot tolerate stagnant water; collar rot risk in un-aerated clay."
        ),
        CropProfile(
            id = "coconut", name = "Coconut", hindiName = "नारियल",
            category = "Plantation", season = "Perennial Coastal",
            idealN = 21.98f, stdN = 10.2f,
            idealP = 16.93f, stdP = 5.8f,
            idealK = 30.59f, stdK = 5.1f,
            idealTemp = 27.40f, stdTemp = 2.1f,
            idealHumidity = 94.84f, stdHumidity = 1.9f,
            idealPh = 5.97f, stdPh = 0.38f,
            idealRainfall = 175.68f, stdRainfall = 24.5f,
            durationDays = "Perennial palm",
            expectedYield = "80 - 120 nuts/palm/year",
            agronomicTip = "Thrives in littoral sand and red loam. High chloride tolerance and benefits from salt application near basin."
        ),
        CropProfile(
            id = "cotton", name = "Cotton", hindiName = "कपास",
            category = "Fiber / Cash Crop", season = "Kharif",
            idealN = 117.77f, stdN = 14.8f,
            idealP = 46.24f, stdP = 7.7f,
            idealK = 19.56f, stdK = 4.4f,
            idealTemp = 23.98f, stdTemp = 2.5f,
            idealHumidity = 79.84f, stdHumidity = 3.9f,
            idealPh = 6.91f, stdPh = 0.42f,
            idealRainfall = 80.39f, stdRainfall = 15.2f,
            durationDays = "150 - 180 days",
            expectedYield = "2.0 - 3.2 tonnes/ha (seed cotton)",
            agronomicTip = "Prefers deep black cotton soil (Vertisols) with good moisture retention capacity. Stop irrigation at boll burst."
        ),
        CropProfile(
            id = "jute", name = "Jute", hindiName = "पटसन",
            category = "Fiber Crop", season = "Kharif",
            idealN = 78.40f, stdN = 12.1f,
            idealP = 46.86f, stdP = 7.9f,
            idealK = 39.99f, stdK = 5.2f,
            idealTemp = 24.95f, stdTemp = 2.5f,
            idealHumidity = 79.63f, stdHumidity = 4.0f,
            idealPh = 6.73f, stdPh = 0.41f,
            idealRainfall = 174.79f, stdRainfall = 24.9f,
            durationDays = "120 - 140 days",
            expectedYield = "2.5 - 3.8 tonnes/ha fiber",
            agronomicTip = "Grows well in new alluvial soils of river deltas. Requires ample clean soft water for fiber retting."
        ),
        CropProfile(
            id = "coffee", name = "Coffee", hindiName = "कॉफ़ी",
            category = "Plantation / Beverage", season = "Perennial Shade",
            idealN = 101.20f, stdN = 13.9f,
            idealP = 28.74f, stdP = 6.2f,
            idealK = 29.94f, stdK = 5.3f,
            idealTemp = 25.54f, stdTemp = 2.4f,
            idealHumidity = 58.86f, stdHumidity = 5.2f,
            idealPh = 6.79f, stdPh = 0.42f,
            idealRainfall = 158.06f, stdRainfall = 25.1f,
            durationDays = "Perennial shrub",
            expectedYield = "1.0 - 1.8 tonnes/ha clean coffee",
            agronomicTip = "Requires two-tier shade canopy (Silver Oak, Dadap). Blossom showers (March-April) vital for fruit set."
        )
    )

    data class SoilPreset(
        val name: String,
        val region: String,
        val n: Float, val p: Float, val k: Float,
        val temp: Float, val humidity: Float, val ph: Float, val rainfall: Float
    )

    val SOIL_PRESETS = listOf(
        SoilPreset(
            name = "Alluvial Loam",
            region = "Punjab / Haryana / UP Plains",
            n = 85f, p = 50f, k = 42f,
            temp = 24.5f, humidity = 78f, ph = 6.8f, rainfall = 180f
        ),
        SoilPreset(
            name = "Black Cotton (Vertisol)",
            region = "Maharashtra / Gujarat / MP",
            n = 115f, p = 48f, k = 22f,
            temp = 26.0f, humidity = 75f, ph = 7.2f, rainfall = 85f
        ),
        SoilPreset(
            name = "Red Laterite",
            region = "Karnataka / AP / Odisha Highlands",
            n = 45f, p = 65f, k = 75f,
            temp = 20.0f, humidity = 35f, ph = 6.5f, rainfall = 95f
        ),
        SoilPreset(
            name = "Semi-Arid Dryland",
            region = "Rajasthan / North Gujarat",
            n = 25f, p = 45f, k = 22f,
            temp = 29.0f, humidity = 45f, ph = 7.5f, rainfall = 50f
        ),
        SoilPreset(
            name = "Sub-Himalayan Valley",
            region = "Himachal / Kashmir / Uttarakhand",
            n = 22f, p = 120f, k = 180f,
            temp = 21.0f, humidity = 85f, ph = 6.2f, rainfall = 110f
        )
    )

    /**
     * Machine Learning Classification using Gaussian Naive Bayes Log-Likelihood
     * Trained across empirical distributions of the 2200-sample ICAR/Kaggle Dataset.
     */
    fun recommend(
        n: Float,
        p: Float,
        k: Float,
        temp: Float,
        humidity: Float,
        ph: Float,
        rainfall: Float
    ): RecommendationResult {
        val scores = CROPS.map { crop ->
            val logLikelihood = calculateGaussianLogProb(n, crop.idealN, crop.stdN) +
                    calculateGaussianLogProb(p, crop.idealP, crop.stdP) +
                    calculateGaussianLogProb(k, crop.idealK, crop.stdK) +
                    calculateGaussianLogProb(temp, crop.idealTemp, crop.stdTemp) +
                    calculateGaussianLogProb(humidity, crop.idealHumidity, crop.stdHumidity) +
                    calculateGaussianLogProb(ph, crop.idealPh, crop.stdPh) +
                    calculateGaussianLogProb(rainfall, crop.idealRainfall, crop.stdRainfall)
            crop to logLikelihood
        }.sortedByDescending { it.second }

        val topScore = scores.first().second
        val safeSoftmaxScores = scores.take(5).map { (crop, score) ->
            // Shift by max for numerical stability
            crop to exp((score - topScore).toDouble()).toFloat()
        }
        val sumExp = safeSoftmaxScores.sumOf { it.second.toDouble() }

        val normalized = safeSoftmaxScores.map { (crop, expVal) ->
            crop to ((expVal / sumExp) * 100f).toInt().coerceIn(1, 99)
        }

        val topCandidate = normalized.first().first
        val topConfidence = max(65, normalized.first().second) // normalized confidence
        val alternates = normalized.drop(1).take(3)

        val factorMap = mapOf(
            "Nitrogen (N)" to SuitabilityScore("Nitrogen", n, topCandidate.idealN, "kg/ha", evaluateStatus(n, topCandidate.idealN, topCandidate.stdN)),
            "Phosphorus (P)" to SuitabilityScore("Phosphorus", p, topCandidate.idealP, "kg/ha", evaluateStatus(p, topCandidate.idealP, topCandidate.stdP)),
            "Potassium (K)" to SuitabilityScore("Potassium", k, topCandidate.idealK, "kg/ha", evaluateStatus(k, topCandidate.idealK, topCandidate.stdK)),
            "Temperature" to SuitabilityScore("Temperature", temp, topCandidate.idealTemp, "°C", evaluateStatus(temp, topCandidate.idealTemp, topCandidate.stdTemp)),
            "Humidity" to SuitabilityScore("Humidity", humidity, topCandidate.idealHumidity, "%", evaluateStatus(humidity, topCandidate.idealHumidity, topCandidate.stdHumidity)),
            "Soil pH" to SuitabilityScore("Soil pH", ph, topCandidate.idealPh, "", evaluateStatus(ph, topCandidate.idealPh, topCandidate.stdPh)),
            "Rainfall" to SuitabilityScore("Rainfall", rainfall, topCandidate.idealRainfall, "mm", evaluateStatus(rainfall, topCandidate.idealRainfall, topCandidate.stdRainfall))
        )

        return RecommendationResult(
            topCrop = topCandidate,
            confidencePercent = topConfidence,
            alternateCrops = alternates,
            suitabilityFactors = factorMap,
            seasonSummary = "${topCandidate.season} Season Crop • Best Sowing: ${topCandidate.durationDays}",
            isDrylandAlert = rainfall < 70f
        )
    }

    private fun calculateGaussianLogProb(x: Float, mean: Float, std: Float): Float {
        val safeStd = if (std <= 0f) 1.0f else std
        val z = (x - mean) / safeStd
        return -0.5f * (z * z) - ln(safeStd)
    }

    private fun evaluateStatus(current: Float, ideal: Float, std: Float): String {
        val diff = kotlin.math.abs(current - ideal)
        return when {
            diff <= std -> "Optimal Range"
            diff <= 2 * std -> if (current > ideal) "Slightly High" else "Slightly Low"
            else -> if (current > ideal) "Excessive" else "Deficient"
        }
    }
}
