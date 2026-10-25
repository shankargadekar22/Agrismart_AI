package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AgriCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusPill
import com.example.ui.theme.AgriPrimaryLight
import com.example.ui.theme.AgriTertiaryLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (ScreenDestination) -> Unit
) {
    val user by viewModel.activeUser.collectAsState()
    val weather by viewModel.weatherState.collectAsState()
    val commodities by viewModel.commodities.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Farmer Welcome Hero Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0F381E),
                                    Color(0xFF1B5E20),
                                    Color(0xFF2E7D32)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Namaste, ${user?.name ?: "Kisan Mitra"}!",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${user?.village ?: "Smart Krushi Gram"} • ${user?.farmSizeAcres ?: 4.0} Acres",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFC8E6C9)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF2E7D32))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Kharif Season",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD54F)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DashboardQuickStat(
                                icon = Icons.Default.Agriculture,
                                label = "Soil Type",
                                value = user?.soilType ?: "Black Cotton"
                            )
                            DashboardQuickStat(
                                icon = Icons.Default.WaterDrop,
                                label = "Irrigation",
                                value = user?.irrigationType ?: "Drip / Furrow"
                            )
                        }
                    }
                }
            }
        }

        // Live Agro Weather Quick Strip
        item {
            AgriCard(onClick = { onNavigate(ScreenDestination.WEATHER) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF9C4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Weather",
                                tint = Color(0xFFF57F17),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = weather?.locationName ?: "Agro-Weather Station",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${weather?.temperatureC ?: 28.0}°C • ${weather?.weatherCondition ?: "Partly Cloudy"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    weather?.sprayAdvisory?.let { spray ->
                        StatusPill(
                            text = if (spray.isSafe) "Spray Safe" else "Don't Spray",
                            status = if (spray.isSafe) "optimal" else "danger"
                        )
                    }
                }
            }
        }

        // Core AI Features Grid
        item {
            SectionHeader(
                title = "Intelligent Farming Hub",
                subtitle = "Decision support powered by verified agronomic ML models"
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FeatureHubCard(
                    title = "1. Crop Recommendation AI",
                    subtitle = "Predict optimal crops based on N-P-K, pH, temperature, humidity, and rainfall using our 22-crop Gaussian ML model.",
                    badge = "Machine Learning",
                    badgeColor = Color(0xFFE8F5E9),
                    badgeTextColor = SuccessGreen,
                    icon = Icons.Default.Eco,
                    iconTint = AgriPrimaryLight,
                    buttonTag = "card_crop_ai",
                    onClick = { onNavigate(ScreenDestination.CROP_AI) }
                )

                FeatureHubCard(
                    title = "2. Plant Leaf Disease Diagnosis",
                    subtitle = "Upload leaf photos to analyze necrotic spotting, chlorosis, and pathological symptoms with organic & chemical cure protocols.",
                    badge = "Computer Vision",
                    badgeColor = Color(0xFFFFF3E0),
                    badgeTextColor = Color(0xFFE65100),
                    icon = Icons.Default.Search,
                    iconTint = Color(0xFFE65100),
                    buttonTag = "card_disease_ai",
                    onClick = { onNavigate(ScreenDestination.DISEASE_AI) }
                )

                FeatureHubCard(
                    title = "3. Fertilizer Calculator",
                    subtitle = "Calculate Urea, DAP, and MOP bag requirements per acre. Includes basal & top-dressing split application schedules.",
                    badge = "ICAR Nutrition",
                    badgeColor = Color(0xFFE0F2F1),
                    badgeTextColor = Color(0xFF00796B),
                    icon = Icons.Default.Science,
                    iconTint = Color(0xFF00796B),
                    buttonTag = "card_fertilizer",
                    onClick = { onNavigate(ScreenDestination.FERTILIZER) }
                )

                FeatureHubCard(
                    title = "4. Agro Weather & Advisory",
                    subtitle = "5-day hyper-local forecast, spray-safe wind window, evapotranspiration loss, and foliar fungal disease warnings.",
                    badge = "Live Meteorological",
                    badgeColor = Color(0xFFE1F5FE),
                    badgeTextColor = Color(0xFF0277BD),
                    icon = Icons.Default.Cloud,
                    iconTint = Color(0xFF0277BD),
                    buttonTag = "card_weather",
                    onClick = { onNavigate(ScreenDestination.WEATHER) }
                )
            }
        }

        // Mandi Market Rates Snapshot
        item {
            SectionHeader(
                title = "Mandi APMC Market Prices",
                subtitle = "Live daily modal rates & arrivals across agricultural mandis",
                trailingAction = {
                    Text(
                        text = "View All >",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onNavigate(ScreenDestination.MANDI) }
                    )
                }
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                commodities.take(3).forEach { commodity ->
                    AgriCard(onClick = { onNavigate(ScreenDestination.MANDI) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = commodity.commodity,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(${commodity.hindiName})",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "${commodity.market}, ${commodity.state}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${commodity.modalPriceQuintal} / Qtl",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = if (commodity.changePercent >= 0) "+${commodity.changePercent}% today" else "${commodity.changePercent}% today",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (commodity.changePercent >= 0) SuccessGreen else Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }

        // Final Year Project Info Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(ScreenDestination.PROJECT_DOCS) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Project Documentation",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Final-Year Engineering Project Docs",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Architecture diagrams, FastAPI backend code, dataset CSVs, and viva examination guide.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun DashboardQuickStat(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0x33FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, fontSize = 10.sp, color = Color(0xFFA5D6A7))
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
private fun FeatureHubCard(
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    badgeTextColor: Color,
    icon: ImageVector,
    iconTint: Color,
    buttonTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(buttonTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(iconTint.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Launch Tool",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
