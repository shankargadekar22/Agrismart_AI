package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.components.AgriBottomNav
import com.example.ui.components.AgriHeader
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CropRecommendationScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DiseaseDetectionScreen
import com.example.ui.screens.FertilizerScreen
import com.example.ui.screens.MandiPricesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProjectDocsScreen
import com.example.ui.screens.WeatherScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SmartKrushiApp(viewModel)
            }
        }
    }
}

@Composable
fun SmartKrushiApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeUser by viewModel.activeUser.collectAsState()

    // Handle back button on sub-screens to return to Dashboard
    if (currentScreen != ScreenDestination.DASHBOARD) {
        BackHandler {
            viewModel.navigateTo(ScreenDestination.DASHBOARD)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_scaffold"),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                AgriHeader(
                    farmerName = activeUser?.name ?: "Kisan Mitra",
                    onProfileClick = { viewModel.navigateTo(ScreenDestination.PROFILE) },
                    onDocsClick = { viewModel.navigateTo(ScreenDestination.PROJECT_DOCS) }
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                AgriBottomNav(
                    currentDestination = currentScreen,
                    onNavigate = { destination ->
                        viewModel.navigateTo(destination)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Crossfade(
                targetState = currentScreen,
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    ScreenDestination.DASHBOARD -> DashboardScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                    ScreenDestination.CROP_AI -> CropRecommendationScreen(
                        viewModel = viewModel
                    )
                    ScreenDestination.DISEASE_AI -> DiseaseDetectionScreen(
                        viewModel = viewModel
                    )
                    ScreenDestination.FERTILIZER -> FertilizerScreen(
                        viewModel = viewModel
                    )
                    ScreenDestination.WEATHER -> WeatherScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                    ScreenDestination.MANDI -> MandiPricesScreen(
                        viewModel = viewModel
                    )
                    ScreenDestination.PROFILE -> ProfileScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                    ScreenDestination.PROJECT_DOCS -> ProjectDocsScreen()
                    ScreenDestination.AUTH -> AuthScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
            }
        }
    }
}
