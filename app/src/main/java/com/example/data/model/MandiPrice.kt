package com.example.data.model

data class MandiCommodity(
    val commodity: String,
    val hindiName: String,
    val market: String,
    val state: String,
    val modalPriceQuintal: Int, // ₹ / Quintal
    val minPrice: Int,
    val maxPrice: Int,
    val changePercent: Float, // e.g. +1.8%
    val arrivalTonnes: Int,
    val qualityGrade: String
)

object MandiPriceService {

    val COMMODITIES = listOf(
        MandiCommodity(
            commodity = "Wheat (Sharbati/Mill)",
            hindiName = "गेहूं",
            market = "Khanna APMC",
            state = "Punjab",
            modalPriceQuintal = 2480,
            minPrice = 2350,
            maxPrice = 2650,
            changePercent = 1.6f,
            arrivalTonnes = 1250,
            qualityGrade = "Grade FAQ (Fair Average Quality)"
        ),
        MandiCommodity(
            commodity = "Paddy (Basmati 1121)",
            hindiName = "बासमती धान",
            market = "Karnal APMC",
            state = "Haryana",
            modalPriceQuintal = 4150,
            minPrice = 3900,
            maxPrice = 4380,
            changePercent = 2.8f,
            arrivalTonnes = 840,
            qualityGrade = "Extra Long Grain"
        ),
        MandiCommodity(
            commodity = "Cotton (Medium Staple)",
            hindiName = "कपास",
            market = "Rajkot APMC",
            state = "Gujarat",
            modalPriceQuintal = 7350,
            minPrice = 7100,
            maxPrice = 7600,
            changePercent = -0.8f,
            arrivalTonnes = 2100,
            qualityGrade = "29mm Staple"
        ),
        MandiCommodity(
            commodity = "Soybean (Yellow)",
            hindiName = "सोयाबीन",
            market = "Indore APMC",
            state = "Madhya Pradesh",
            modalPriceQuintal = 4720,
            minPrice = 4500,
            maxPrice = 4850,
            changePercent = 0.5f,
            arrivalTonnes = 3200,
            qualityGrade = "Oil Content > 18%"
        ),
        MandiCommodity(
            commodity = "Onion (Red)",
            hindiName = "लाल प्याज",
            market = "Lasalgaon APMC",
            state = "Maharashtra",
            modalPriceQuintal = 2150,
            minPrice = 1600,
            maxPrice = 2500,
            changePercent = -3.2f,
            arrivalTonnes = 4500,
            qualityGrade = "Medium Bold"
        ),
        MandiCommodity(
            commodity = "Maize (Hybrid)",
            hindiName = "मक्का",
            market = "Davangere APMC",
            state = "Karnataka",
            modalPriceQuintal = 2190,
            minPrice = 2050,
            maxPrice = 2280,
            changePercent = 1.2f,
            arrivalTonnes = 950,
            qualityGrade = "Moisture < 14%"
        ),
        MandiCommodity(
            commodity = "Mustard (Raya)",
            hindiName = "सरसों",
            market = "Jaipur Mandi",
            state = "Rajasthan",
            modalPriceQuintal = 5620,
            minPrice = 5400,
            maxPrice = 5850,
            changePercent = 2.1f,
            arrivalTonnes = 1800,
            qualityGrade = "Oil Content 42%"
        ),
        MandiCommodity(
            commodity = "Chickpea (Chana Desi)",
            hindiName = "चना",
            market = "Latur APMC",
            state = "Maharashtra",
            modalPriceQuintal = 6100,
            minPrice = 5850,
            maxPrice = 6350,
            changePercent = 0.9f,
            arrivalTonnes = 1400,
            qualityGrade = "Bold Grain"
        )
    )
}
