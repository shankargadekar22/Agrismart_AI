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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ml.FertilizerCalculator
import com.example.ui.components.AgriCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AgriPrimaryLight
import com.example.ui.theme.AgriTertiaryLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.MainViewModel

@Composable
fun FertilizerScreen(viewModel: MainViewModel) {
    val selectedCrop by viewModel.fertilizerCrop.collectAsState()
    val acresStr by viewModel.fertilizerAcres.collectAsState()
    val soilN by viewModel.soilN.collectAsState()
    val soilP by viewModel.soilP.collectAsState()
    val soilK by viewModel.soilK.collectAsState()
    val result by viewModel.fertilizerResult.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionHeader(
                title = "Fertilizer Recommendation Calculator",
                subtitle = "Precise N-P-K nutrient deficit calculation and split bag dosage",
                icon = Icons.Default.Science
            )
        }

        // Crop Selection Row
        item {
            Column {
                Text(
                    text = "Select Target Crop:",
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
                    FertilizerCalculator.CROP_REQUIREMENTS.forEach { crop ->
                        val isSelected = selectedCrop == crop.cropName
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) AgriPrimaryLight else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable {
                                viewModel.fertilizerCrop.value = crop.cropName
                                viewModel.runFertilizerCalculation()
                            }
                        ) {
                            Text(
                                text = crop.cropName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Inputs: Farm Area & Current Soil Levels
        item {
            AgriCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Farm Land & Soil Test Values",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = acresStr,
                        onValueChange = {
                            viewModel.fertilizerAcres.value = it
                        },
                        label = { Text("Farm Area (in Acres)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("fertilizer_acres_input"),
                        singleLine = true
                    )

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Soil Available Nitrogen (N)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text(text = "${soilN.toInt()} kg/acre", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgriPrimaryLight)
                        }
                        Slider(
                            value = soilN,
                            onValueChange = { viewModel.soilN.value = it },
                            valueRange = 0f..80f,
                            colors = SliderDefaults.colors(thumbColor = AgriPrimaryLight, activeTrackColor = AgriPrimaryLight)
                        )
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Soil Available Phosphorus (P)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text(text = "${soilP.toInt()} kg/acre", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgriTertiaryLight)
                        }
                        Slider(
                            value = soilP,
                            onValueChange = { viewModel.soilP.value = it },
                            valueRange = 0f..60f,
                            colors = SliderDefaults.colors(thumbColor = AgriTertiaryLight, activeTrackColor = AgriTertiaryLight)
                        )
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Soil Available Potassium (K)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text(text = "${soilK.toInt()} kg/acre", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7C3AED))
                        }
                        Slider(
                            value = soilK,
                            onValueChange = { viewModel.soilK.value = it },
                            valueRange = 0f..60f,
                            colors = SliderDefaults.colors(thumbColor = Color(0xFF7C3AED), activeTrackColor = Color(0xFF7C3AED))
                        )
                    }
                }
            }
        }

        // Calculate Button
        item {
            Button(
                onClick = { viewModel.runFertilizerCalculation() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("calculate_fertilizer_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AgriPrimaryLight
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Calculate, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Calculate Fertilizer Dosage",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Results Section
        item {
            result?.let { plan ->
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Fertilizer Bag Counts
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Required Commercial Fertilizer Bags (${plan.farmAreaAcres} Acres):",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AgriPrimaryLight
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                BagMetricCard(
                                    bagName = "Urea (46% N)",
                                    count = plan.ureaBags.toInt(),
                                    subtext = "45 kg bags",
                                    color = Color(0xFF1B5E20)
                                )
                                BagMetricCard(
                                    bagName = "DAP (18:46:0)",
                                    count = plan.dapBags.toInt(),
                                    subtext = "50 kg bags",
                                    color = Color(0xFFD97706)
                                )
                                BagMetricCard(
                                    bagName = "MOP (60% K)",
                                    count = plan.mopBags.toInt(),
                                    subtext = "50 kg bags",
                                    color = Color(0xFF7C3AED)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Nature,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Organic Manure Equivalent: ${plan.vermicompostTonnes} Tonnes Vermicompost",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Split Application Schedule
                    AgriCard {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Split Application Schedule",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            plan.splitSchedule.forEachIndexed { index, stage ->
                                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${index + 1}. ${stage.stageName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = stage.timing,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = "Urea: ${stage.ureaShare} • DAP: ${stage.dapShare} • MOP: ${stage.mopShare}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SuccessGreen
                                    )
                                    Text(
                                        text = stage.note,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (index < plan.splitSchedule.lastIndex) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                            }
                        }
                    }

                    // Safety Guidelines
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = WarningAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Agricultural Safety & Handling Guidelines",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFFB45309)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            plan.safetyGuidelines.forEach { rule ->
                                Text(
                                    text = "• $rule",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Note: ${plan.limitations}",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BagMetricCard(
    bagName: String,
    count: Int,
    subtext: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$count", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = bagName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtext, fontSize = 9.sp, color = Color.Gray)
        }
    }
}
