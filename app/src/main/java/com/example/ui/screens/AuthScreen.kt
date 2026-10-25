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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AgriCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AgriPrimaryLight
import com.example.ui.theme.DangerRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun AuthScreen(
    viewModel: MainViewModel,
    onNavigate: (ScreenDestination) -> Unit
) {
    val isRegister by viewModel.isRegisterMode.collectAsState()
    val name by viewModel.authName.collectAsState()
    val phone by viewModel.authPhone.collectAsState()
    val village by viewModel.authVillage.collectAsState()
    val farmSize by viewModel.authFarmSize.collectAsState()
    val soilType by viewModel.authSoilType.collectAsState()
    val irrigation by viewModel.authIrrigation.collectAsState()
    val pin by viewModel.authPin.collectAsState()
    val error by viewModel.authError.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(AgriPrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Smart Krushi AI Portal",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AgriPrimaryLight
                )
                Text(
                    text = "Intelligent Agriculture System Farmer Authentication",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Mode Switcher Tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (!isRegister) Color.White else Color.Transparent)
                        .clickable {
                            viewModel.isRegisterMode.value = false
                            viewModel.authError.value = null
                        }
                        .padding(vertical = 10.dp)
                        .testTag("tab_login"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Farmer Login",
                        fontWeight = if (!isRegister) FontWeight.Bold else FontWeight.Normal,
                        color = if (!isRegister) AgriPrimaryLight else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isRegister) Color.White else Color.Transparent)
                        .clickable {
                            viewModel.isRegisterMode.value = true
                            viewModel.authError.value = null
                        }
                        .padding(vertical = 10.dp)
                        .testTag("tab_register"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "New Registration",
                        fontWeight = if (isRegister) FontWeight.Bold else FontWeight.Normal,
                        color = if (isRegister) AgriPrimaryLight else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Error message banner
        error?.let { err ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = DangerRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = err,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = DangerRed
                        )
                    }
                }
            }
        }

        // Form Fields
        item {
            AgriCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (isRegister) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { viewModel.authName.value = it },
                            label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_name_input"),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { viewModel.authPhone.value = it },
                        label = { Text("Mobile Number (10 digits)") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_phone_input"),
                        singleLine = true
                    )

                    if (isRegister) {
                        OutlinedTextField(
                            value = village,
                            onValueChange = { viewModel.authVillage.value = it },
                            label = { Text("Village / Gram Panchayat") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = farmSize,
                            onValueChange = { viewModel.authFarmSize.value = it },
                            label = { Text("Farm Area (in Acres)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = soilType,
                            onValueChange = { viewModel.authSoilType.value = it },
                            label = { Text("Primary Soil Type (Black/Red/Alluvial)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = irrigation,
                            onValueChange = { viewModel.authIrrigation.value = it },
                            label = { Text("Irrigation (Drip/Sprinkler/Canal/Rainfed)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = pin,
                        onValueChange = { viewModel.authPin.value = it },
                        label = { Text("4-Digit Security PIN") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_pin_input"),
                        singleLine = true
                    )
                }
            }
        }

        // Action Button
        item {
            Button(
                onClick = {
                    if (isRegister) {
                        viewModel.performRegister {
                            onNavigate(ScreenDestination.DASHBOARD)
                        }
                    } else {
                        viewModel.performLogin {
                            onNavigate(ScreenDestination.DASHBOARD)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("auth_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = AgriPrimaryLight),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isRegister) "Register Farmer Profile" else "Login to Dashboard",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Default test login: Phone: 9876543210 • PIN: 1234",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
