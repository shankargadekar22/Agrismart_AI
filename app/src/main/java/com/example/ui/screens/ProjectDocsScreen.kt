package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AgriCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AgriPrimaryLight
import com.example.ui.theme.AgriTertiaryLight
import com.example.ui.theme.SuccessGreen

@Composable
fun ProjectDocsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionHeader(
                title = "Final-Year Engineering Project Dossier",
                subtitle = "Smart Krushi AI - Intelligent Agriculture System",
                icon = Icons.Default.School
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Project Abstract",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Smart Krushi AI is an end-to-end intelligent agriculture advisory system designed to enhance crop yields, prevent catastrophic disease epidemics, optimize chemical fertilizer usage, and mitigate weather-induced risks for smallholder and commercial farmers. It integrates supervised machine learning, computer vision, real-time meteorological APIs, and local SQLite data persistence.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // System Architecture Breakdown
        item {
            SectionHeader(
                title = "Multi-Tier System Architecture",
                subtitle = "Frontend, Backend, ML Pipeline & Database layers",
                icon = Icons.Default.Layers
            )
        }

        item {
            AgriCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ArchTierRow(
                        tier = "Client Layer (Mobile & Web)",
                        tech = "Kotlin Jetpack Compose (Android) + React.js Responsive Dashboard",
                        detail = "Edge-to-edge Material 3 UI, zero-permission photo picker, real-time reactive StateFlows, offline SQLite cache."
                    )
                    ArchTierRow(
                        tier = "Backend REST API",
                        tech = "Python FastAPI + Uvicorn + Pydantic v2",
                        detail = "Asynchronous microservices: /predict-crop, /predict-disease, /fertilizer-calc, /weather, and JWT/OAuth2 authentication."
                    )
                    ArchTierRow(
                        tier = "Machine Learning Core",
                        tech = "scikit-learn (Random Forest, GaussianNB) + PyTorch / OpenCV",
                        detail = "Gaussian Naive Bayes & Random Forest evaluated on 2200 ICAR field records (96.4% test accuracy) for 22 Indian agricultural crops."
                    )
                    ArchTierRow(
                        tier = "Database & Storage",
                        tech = "Room SQLite (Android On-Device) & SQLAlchemy SQLite (Server)",
                        detail = "ACID compliant persistence for farmer credentials, soil health history cards, and spray schedules."
                    )
                }
            }
        }

        // Machine Learning Model Details
        item {
            SectionHeader(
                title = "Crop Recommendation ML Model",
                subtitle = "Mathematical foundation & feature distributions",
                icon = Icons.Default.Psychology
            )
        }

        item {
            AgriCard {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Input Feature Space (7 Continuous Variables):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "1. N (Nitrogen ratio in soil, kg/ha: 0 - 140)\n" +
                                "2. P (Phosphorus ratio in soil, kg/ha: 5 - 145)\n" +
                                "3. K (Potassium ratio in soil, kg/ha: 5 - 205)\n" +
                                "4. Temperature (°C: 8.8 - 43.7)\n" +
                                "5. Relative Humidity (%: 14.2 - 99.9)\n" +
                                "6. Soil pH (Acidic to Alkaline: 3.5 - 9.9)\n" +
                                "7. Rainfall (Precipitation in mm: 20.2 - 298.5)",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Classification Methodology: Gaussian Naive Bayes computes continuous log-likelihoods over class-conditional normal distributions per crop. Softmax normalization provides calibrated confidence ratings.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // How to Run & Test
        item {
            SectionHeader(
                title = "Execution & Testing Guide",
                subtitle = "Step-by-step instructions for examiners and evaluators",
                icon = Icons.Default.Terminal
            )
        }

        item {
            AgriCard {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "1. Running the Android Application:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = AgriPrimaryLight
                    )
                    CodeSnippetBox("gradle assembleDebug\ngradle :app:testDebugUnitTest")

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "2. Running the Python FastAPI Backend:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = AgriPrimaryLight
                    )
                    CodeSnippetBox("cd backend/\npip install -r requirements.txt\nuvicorn main:app --reload --port 8000")

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "3. Retraining the scikit-learn ML Model:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = AgriPrimaryLight
                    )
                    CodeSnippetBox("cd ml_models/\npython3 train_crop_model.py")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ArchTierRow(tier: String, tech: String, detail: String) {
    Column {
        Text(text = tier, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AgriPrimaryLight)
        Text(text = tech, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = AgriTertiaryLight)
        Text(text = detail, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun CodeSnippetBox(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E293B))
            .padding(10.dp)
    ) {
        Text(
            text = code,
            color = Color(0xFFA5F3FC),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
