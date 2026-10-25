package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ml.CropRecommendationEngine
import com.example.ui.components.AgriCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusPill
import com.example.ui.theme.AgriPrimaryLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CropRecommendationScreen(viewModel: MainViewModel) {
    val n by viewModel.nInput.collectAsState()
    val p by viewModel.pInput.collectAsState()
    val k by viewModel.kInput.collectAsState()
    val temp by viewModel.tempInput.collectAsState()
    val humidity by viewModel.humidityInput.collectAsState()
    val ph by viewModel.phInput.collectAsState()
    val rainfall by viewModel.rainfallInput.collectAsState()
    val result by viewModel.recommendationResult.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionHeader(
                title = "Crop Recommendation Engine",
                subtitle = "Multivariate Gaussian classifier trained on 2,200 ICAR field trials across 22 crops",
                icon = Icons.Default.Eco
            )
        }

        // Soil Presets Bar
        item {
            Column {
                Text(
                    text = "Quick Soil Presets (1-Tap Benchmark):",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CropRecommendationEngine.SOIL_PRESETS.forEach { preset ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.clickable {
                                viewModel.applySoilPreset(preset)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = preset.name,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = preset.region.take(22) + "...",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Soil Nutrient Sliders
        item {
            AgriCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Soil Nutrients (from Soil Test Card)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    ParameterSlider(
                        label = "Nitrogen (N)",
                        value = n,
                        range = 0f..140f,
                        unit = "kg/ha",
                        color = Color(0xFF1B5E20),
                        onValueChange = { viewModel.nInput.value = it }
                    )

                    ParameterSlider(
                        label = "Phosphorus (P)",
                        value = p,
                        range = 5f..145f,
                        unit = "kg/ha",
                        color = Color(0xFFD97706),
                        onValueChange = { viewModel.pInput.value = it }
                    )

                    ParameterSlider(
                        label = "Potassium (K)",
                        value = k,
                        range = 5f..205f,
                        unit = "kg/ha",
                        color = Color(0xFF7C3AED),
                        onValueChange = { viewModel.kInput.value = it }
                    )

                    ParameterSlider(
                        label = "Soil pH Level",
                        value = ph,
                        range = 3.5f..9.5f,
                        unit = "pH",
                        color = Color(0xFF0284C7),
                        formatDecimals = 1,
                        onValueChange = { viewModel.phInput.value = it }
                    )
                }
            }
        }

        // Agro-Climatic Parameters
        item {
            AgriCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Agro-Climatic Conditions",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    ParameterSlider(
                        label = "Temperature",
                        value = temp,
                        range = 8f..45f,
                        unit = "°C",
                        color = Color(0xFFEA580C),
                        formatDecimals = 1,
                        onValueChange = { viewModel.tempInput.value = it }
                    )

                    ParameterSlider(
                        label = "Relative Humidity",
                        value = humidity,
                        range = 10f..100f,
                        unit = "%",
                        color = Color(0xFF0891B2),
                        onValueChange = { viewModel.humidityInput.value = it }
                    )

                    ParameterSlider(
                        label = "Rainfall (Annual / Seasonal)",
                        value = rainfall,
                        range = 20f..300f,
                        unit = "mm",
                        color = Color(0xFF2563EB),
                        onValueChange = { viewModel.rainfallInput.value = it }
                    )
                }
            }
        }

        // Primary Predict Button
        item {
            Button(
                onClick = { viewModel.runCropRecommendation() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("predict_crop_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AgriPrimaryLight
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recommend Best Crop (ML)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Recommendation Result Card
        item {
            result?.let { rec ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF1F8E9)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = rec.topCrop.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriPrimaryLight
                                    )
                                }
                                Text(
                                    text = "${rec.topCrop.hindiName} • ${rec.topCrop.category}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(AgriPrimaryLight)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${rec.confidencePercent}% Match",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Match meter progress
                        LinearProgressIndicator(
                            progress = { rec.confidencePercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = SuccessGreen,
                            trackColor = Color(0xFFC8E6C9)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Season & Yield Info Box
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Optimal Season", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = rec.topCrop.season, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(text = "Maturity Duration", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = rec.topCrop.durationDays, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(text = "Expected Yield", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = rec.topCrop.expectedYield, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Agronomic Advice Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = AgriPrimaryLight,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = rec.topCrop.agronomicTip,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Alternate Crops
                        Text(
                            text = "Alternate Suitable Crops:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        rec.alternateCrops.forEach { (altCrop, altConf) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${altCrop.name} (${altCrop.hindiName})",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                StatusPill(text = "$altConf% Match", status = "info")
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Suitability Breakdown Checklist
                        Text(
                            text = "Soil & Climate Suitability Factors:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        rec.suitabilityFactors.values.forEach { factor ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${factor.label}: ${factor.current.toInt()} vs Ideal ${factor.ideal.toInt()} ${factor.unit}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                StatusPill(
                                    text = factor.status,
                                    status = factor.status
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ParameterSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    unit: String,
    color: Color,
    formatDecimals: Int = 0,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (formatDecimals == 1) "%.1f %s".format(value, unit) else "${value.toInt()} $unit",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color,
                inactiveTrackColor = color.copy(alpha = 0.2f)
            )
        )
    }
}
