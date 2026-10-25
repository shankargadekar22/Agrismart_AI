package com.example.data.ml

import kotlin.math.ceil
import kotlin.math.max

data class CropNutrientRequirement(
    val cropName: String,
    val targetN: Float, // kg/acre
    val targetP: Float, // kg/acre
    val targetK: Float, // kg/acre
    val organicManureTonnesPerAcre: Float,
    val bioFertilizerNote: String,
    val secondaryNutrients: String
)

data class FertilizerCalculationResult(
    val cropName: String,
    val farmAreaAcres: Float,
    val deficitN: Float,
    val deficitP: Float,
    val deficitK: Float,
    // Fertilizer quantities (in bags)
    val ureaBags: Float,        // 45 kg bag (46% N)
    val dapBags: Float,         // 50 kg bag (18% N, 46% P)
    val mopBags: Float,         // 50 kg bag (60% K)
    val vermicompostTonnes: Float,
    val splitSchedule: List<FertilizerSplitStage>,
    val safetyGuidelines: List<String>,
    val limitations: String
)

data class FertilizerSplitStage(
    val stageName: String,
    val timing: String,
    val ureaShare: String,
    val dapShare: String,
    val mopShare: String,
    val note: String
)

object FertilizerCalculator {

    val CROP_REQUIREMENTS = listOf(
        CropNutrientRequirement(
            cropName = "Rice (Paddy)",
            targetN = 48f, targetP = 24f, targetK = 24f,
            organicManureTonnesPerAcre = 4.0f,
            bioFertilizerNote = "Apply Azospirillum and Phosphobacteria (PSB) @ 2 kg/acre mixed with 100 kg FYM.",
            secondaryNutrients = "Apply Zinc Sulphate (21%) @ 10 kg/acre as basal to prevent 'Khaira' zinc deficiency."
        ),
        CropNutrientRequirement(
            cropName = "Wheat",
            targetN = 48f, targetP = 24f, targetK = 16f,
            organicManureTonnesPerAcre = 3.5f,
            bioFertilizerNote = "Seed inoculation with Azotobacter & PSB cultures prior to sowing.",
            secondaryNutrients = "In light soils, apply Sulphur @ 8 kg/acre through Gypsum or Bentonite Sulphur."
        ),
        CropNutrientRequirement(
            cropName = "Cotton",
            targetN = 60f, targetP = 30f, targetK = 30f,
            organicManureTonnesPerAcre = 5.0f,
            bioFertilizerNote = "Azotobacter and Mycorrhiza (VAM) soil application at sowing.",
            secondaryNutrients = "Foliar spray of 2% Magnesium Sulphate + 1% 19:19:19 during peak flowering."
        ),
        CropNutrientRequirement(
            cropName = "Maize (Corn)",
            targetN = 48f, targetP = 24f, targetK = 20f,
            organicManureTonnesPerAcre = 4.0f,
            bioFertilizerNote = "Azospirillum @ 2 kg/acre with vermicompost.",
            secondaryNutrients = "Zinc Sulphate @ 10 kg/acre basal; critical for ear development."
        ),
        CropNutrientRequirement(
            cropName = "Soybean",
            targetN = 12f, targetP = 32f, targetK = 16f,
            organicManureTonnesPerAcre = 2.5f,
            bioFertilizerNote = "Bradyrhizobium japonicum seed inoculation (Rhizobium fixes 80% of crop N).",
            secondaryNutrients = "Apply Sulphur @ 10 kg/acre to boost oil content and seed protein."
        ),
        CropNutrientRequirement(
            cropName = "Tomato",
            targetN = 60f, targetP = 40f, targetK = 50f,
            organicManureTonnesPerAcre = 6.0f,
            bioFertilizerNote = "Root dip in Pseudomonas fluorescens + PSB suspension before transplanting.",
            secondaryNutrients = "Calcium Nitrate foliar spray (5g/L) to prevent Blossom End Rot."
        ),
        CropNutrientRequirement(
            cropName = "Sugarcane",
            targetN = 100f, targetP = 40f, targetK = 48f,
            organicManureTonnesPerAcre = 10.0f,
            bioFertilizerNote = "Gluconacetobacter diazotrophicus and PSB furrow application.",
            secondaryNutrients = "Ferrous Sulphate (10 kg/acre) in high pH calcareous black soils."
        ),
        CropNutrientRequirement(
            cropName = "Onion",
            targetN = 40f, targetP = 20f, targetK = 40f,
            organicManureTonnesPerAcre = 5.0f,
            bioFertilizerNote = "VAM (Vesicular Arbuscular Mycorrhiza) @ 4 kg/acre.",
            secondaryNutrients = "Sulphur @ 15 kg/acre enhances bulb pungency, firmness and storage life."
        )
    )

    fun calculate(
        cropName: String,
        soilN: Float, // Current soil Nitrogen (kg/acre)
        soilP: Float, // Current soil Phosphorus (kg/acre)
        soilK: Float, // Current soil Potassium (kg/acre)
        acres: Float = 1.0f
    ): FertilizerCalculationResult {
        val crop = CROP_REQUIREMENTS.find { it.cropName.equals(cropName, ignoreCase = true) }
            ?: CROP_REQUIREMENTS.first()

        val safeAcres = acres.coerceAtLeast(0.1f)

        // Nutrient Deficit per acre
        val deficitNPerAcre = max(0f, crop.targetN - (soilN * 0.4f)) // 40% soil availability factor
        val deficitPPerAcre = max(0f, crop.targetP - (soilP * 0.35f))
        val deficitKPerAcre = max(0f, crop.targetK - (soilK * 0.45f))

        val totalDeficitN = deficitNPerAcre * safeAcres
        val totalDeficitP = deficitPPerAcre * safeAcres
        val totalDeficitK = deficitKPerAcre * safeAcres

        // DAP contains 46% P2O5 and 18% N
        // 50kg bag of DAP provides: 23 kg P2O5 and 9 kg N
        val dapBags = ceil(totalDeficitP / 23f)
        val nitrogenSuppliedByDap = dapBags * 9f

        // Remaining N to be supplied by Urea
        // 45kg bag of Urea (46% N) supplies: 20.7 kg N
        val remainingN = max(0f, totalDeficitN - nitrogenSuppliedByDap)
        val ureaBags = ceil(remainingN / 20.7f)

        // MOP contains 60% K2O
        // 50kg bag of MOP supplies: 30 kg K2O
        val mopBags = ceil(totalDeficitK / 30f)

        val vermicompostTonnes = crop.organicManureTonnesPerAcre * safeAcres * 0.4f // equivalent vermicompost

        val splitSchedule = listOf(
            FertilizerSplitStage(
                stageName = "Basal Dose",
                timing = "At the time of sowing / final land preparation",
                ureaShare = if (ureaBags > 0) "${(ureaBags * 0.33f).toInt().coerceAtLeast(1)} bags" else "0 bags",
                dapShare = "$dapBags bags (100%)",
                mopShare = if (mopBags > 0) "${(mopBags * 0.5f).toInt().coerceAtLeast(1)} bags" else "0 bags",
                note = "Broadcast and incorporate 5-8 cm deep into soil before sowing. Apply full DAP as basal."
            ),
            FertilizerSplitStage(
                stageName = "1st Top Dressing (Vegetative)",
                timing = "25 - 35 days after sowing (Active Tillering / Branching)",
                ureaShare = if (ureaBags > 0) "${(ureaBags * 0.33f).toInt().coerceAtLeast(1)} bags" else "0 bags",
                dapShare = "None",
                mopShare = "None",
                note = "Broadcast in moist soil followed by light irrigation. Avoid water stagnation."
            ),
            FertilizerSplitStage(
                stageName = "2nd Top Dressing (Reproductive)",
                timing = "50 - 65 days after sowing (Panicle / Flowering onset)",
                ureaShare = if (ureaBags > 0) "${(ureaBags * 0.34f).toInt().coerceAtLeast(1)} bags" else "0 bags",
                dapShare = "None",
                mopShare = if (mopBags > 0) "${(mopBags * 0.5f).toInt().coerceAtLeast(1)} bags" else "0 bags",
                note = "Helps in grain filling and test weight. Apply remaining Potash if split is recommended."
            )
        )

        val safetyGuidelines = listOf(
            "Never mix DAP / Superphosphate with Zinc Sulphate directly in the applicator tank; it forms insoluble zinc phosphate.",
            "Neem-Coated Urea is mandatory to minimize ammonia volatilization and nitrous leaching.",
            "Always maintain sufficient soil moisture before top dressing Urea to prevent root scorch.",
            "Wear agricultural nitrile gloves and wash exposed skin with clean water after handling fertilizers."
        )

        val limitations = "Calculation is based on standard ICAR agro-climatic nutrient uptake models. For exact precision, compare against your latest certified Soil Health Card (SHC) issued by the Ministry of Agriculture."

        return FertilizerCalculationResult(
            cropName = cropName,
            farmAreaAcres = safeAcres,
            deficitN = totalDeficitN,
            deficitP = totalDeficitP,
            deficitK = totalDeficitK,
            ureaBags = ureaBags,
            dapBags = dapBags,
            mopBags = mopBags,
            vermicompostTonnes = vermicompostTonnes,
            splitSchedule = splitSchedule,
            safetyGuidelines = safetyGuidelines,
            limitations = limitations
        )
    }
}
