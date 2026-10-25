package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AgriDatabase
import com.example.data.local.CropHistoryEntity
import com.example.data.local.DiseaseHistoryEntity
import com.example.data.local.FertilizerHistoryEntity
import com.example.data.local.UserEntity
import com.example.data.ml.CropRecommendationEngine
import com.example.data.ml.DiseaseDetectionEngine
import com.example.data.ml.DiseaseDiagnosis
import com.example.data.ml.FertilizerCalculationResult
import com.example.data.ml.FertilizerCalculator
import com.example.data.ml.RecommendationResult
import com.example.data.model.MandiCommodity
import com.example.data.model.MandiPriceService
import com.example.data.network.AgriLocation
import com.example.data.network.AgroWeatherDisplay
import com.example.data.network.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenDestination {
    DASHBOARD,
    CROP_AI,
    DISEASE_AI,
    FERTILIZER,
    WEATHER,
    MANDI,
    PROFILE,
    PROJECT_DOCS,
    AUTH
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AgriDatabase.getDatabase(application)
    private val userDao = db.userDao()
    private val historyDao = db.historyDao()
    private val weatherRepo = WeatherRepository()

    // Navigation state
    private val _currentScreen = MutableStateFlow(ScreenDestination.DASHBOARD)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // Active User from Room Database
    val activeUser: StateFlow<UserEntity?> = userDao.getActiveUserFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // History flows
    val cropHistory: StateFlow<List<CropHistoryEntity>> = historyDao.getCropHistoryFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val diseaseHistory: StateFlow<List<DiseaseHistoryEntity>> = historyDao.getDiseaseHistoryFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val fertilizerHistory: StateFlow<List<FertilizerHistoryEntity>> = historyDao.getFertilizerHistoryFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Weather State ---
    private val _weatherState = MutableStateFlow<AgroWeatherDisplay?>(null)
    val weatherState: StateFlow<AgroWeatherDisplay?> = _weatherState.asStateFlow()

    private val _isWeatherLoading = MutableStateFlow(false)
    val isWeatherLoading: StateFlow<Boolean> = _isWeatherLoading.asStateFlow()

    private val _selectedLocation = MutableStateFlow(weatherRepo.POPULAR_LOCATIONS.first())
    val selectedLocation: StateFlow<AgriLocation> = _selectedLocation.asStateFlow()

    // --- Crop Recommendation State ---
    val nInput = MutableStateFlow(85f)
    val pInput = MutableStateFlow(48f)
    val kInput = MutableStateFlow(40f)
    val tempInput = MutableStateFlow(24.5f)
    val humidityInput = MutableStateFlow(78f)
    val phInput = MutableStateFlow(6.5f)
    val rainfallInput = MutableStateFlow(160f)

    private val _recommendationResult = MutableStateFlow<RecommendationResult?>(null)
    val recommendationResult: StateFlow<RecommendationResult?> = _recommendationResult.asStateFlow()

    // --- Disease Detection State ---
    private val _selectedLeafBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedLeafBitmap: StateFlow<Bitmap?> = _selectedLeafBitmap.asStateFlow()

    private val _selectedLeafPreset = MutableStateFlow<String?>("Tomato Early Blight")
    val selectedLeafPreset: StateFlow<String?> = _selectedLeafPreset.asStateFlow()

    val selectedCropHint = MutableStateFlow("Tomato")

    private val _diseaseDiagnosis = MutableStateFlow<DiseaseDiagnosis?>(null)
    val diseaseDiagnosis: StateFlow<DiseaseDiagnosis?> = _diseaseDiagnosis.asStateFlow()

    private val _isDiagnosing = MutableStateFlow(false)
    val isDiagnosing: StateFlow<Boolean> = _isDiagnosing.asStateFlow()

    // --- Fertilizer State ---
    val fertilizerCrop = MutableStateFlow("Rice (Paddy)")
    val fertilizerAcres = MutableStateFlow("2.0")
    val soilN = MutableStateFlow(40f)
    val soilP = MutableStateFlow(20f)
    val soilK = MutableStateFlow(22f)

    private val _fertilizerResult = MutableStateFlow<FertilizerCalculationResult?>(null)
    val fertilizerResult: StateFlow<FertilizerCalculationResult?> = _fertilizerResult.asStateFlow()

    // --- Mandi Market State ---
    private val _commodities = MutableStateFlow(MandiPriceService.COMMODITIES)
    val commodities: StateFlow<List<MandiCommodity>> = _commodities.asStateFlow()

    val mandiSearchQuery = MutableStateFlow("")

    // Auth Form State
    val authName = MutableStateFlow("")
    val authPhone = MutableStateFlow("")
    val authVillage = MutableStateFlow("")
    val authFarmSize = MutableStateFlow("3.5")
    val authSoilType = MutableStateFlow("Black Cotton (Vertisol)")
    val authIrrigation = MutableStateFlow("Drip Irrigation")
    val authPin = MutableStateFlow("")
    val authError = MutableStateFlow<String?>(null)
    val isRegisterMode = MutableStateFlow(false)

    init {
        // Ensure default initial user exists if database is fresh
        ensureDefaultFarmer()
        // Load initial weather
        refreshWeather()
        // Compute initial default crop recommendation
        runCropRecommendation()
        // Compute initial default fertilizer plan
        runFertilizerCalculation()
        // Compute initial leaf diagnosis
        runPresetDiagnosis("Tomato Early Blight")
    }

    private fun ensureDefaultFarmer() {
        viewModelScope.launch {
            val user = userDao.getActiveUser()
            if (user == null) {
                userDao.insertUser(
                    UserEntity(
                        name = "Ramesh Patil",
                        phone = "9876543210",
                        village = "Khed, Pune District",
                        farmSizeAcres = 4.5f,
                        soilType = "Black Cotton Soil",
                        irrigationType = "Drip & Canal Irrigation",
                        pinHash = "1234"
                    )
                )
            }
        }
    }

    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun refreshWeather(location: AgriLocation = _selectedLocation.value) {
        _selectedLocation.value = location
        viewModelScope.launch {
            _isWeatherLoading.value = true
            _weatherState.value = weatherRepo.fetchAgroWeather(location)
            _isWeatherLoading.value = false
        }
    }

    fun applySoilPreset(preset: CropRecommendationEngine.SoilPreset) {
        nInput.value = preset.n
        pInput.value = preset.p
        kInput.value = preset.k
        tempInput.value = preset.temp
        humidityInput.value = preset.humidity
        phInput.value = preset.ph
        rainfallInput.value = preset.rainfall
        runCropRecommendation()
    }

    fun runCropRecommendation() {
        val result = CropRecommendationEngine.recommend(
            n = nInput.value,
            p = pInput.value,
            k = kInput.value,
            temp = tempInput.value,
            humidity = humidityInput.value,
            ph = phInput.value,
            rainfall = rainfallInput.value
        )
        _recommendationResult.value = result

        // Save to Room SQLite database
        viewModelScope.launch {
            historyDao.insertCropHistory(
                CropHistoryEntity(
                    nitrogen = nInput.value,
                    phosphorus = pInput.value,
                    potassium = kInput.value,
                    temperature = tempInput.value,
                    humidity = humidityInput.value,
                    ph = phInput.value,
                    rainfall = rainfallInput.value,
                    recommendedCrop = result.topCrop.name,
                    confidence = result.confidencePercent.toFloat(),
                    season = result.topCrop.season
                )
            )
        }
    }

    fun setLeafBitmap(bitmap: Bitmap) {
        _selectedLeafBitmap.value = bitmap
        _selectedLeafPreset.value = null
        viewModelScope.launch {
            _isDiagnosing.value = true
            val diag = DiseaseDetectionEngine.analyzeLeafBitmap(bitmap, selectedCropHint.value)
            _diseaseDiagnosis.value = diag
            _isDiagnosing.value = false

            historyDao.insertDiseaseHistory(
                DiseaseHistoryEntity(
                    cropName = diag.crop,
                    diseaseName = diag.diseaseName,
                    pathogen = diag.pathogen,
                    severity = diag.severity,
                    confidence = diag.confidence.toFloat(),
                    remedySummary = diag.organicTreatment.take(120)
                )
            )
        }
    }

    fun runPresetDiagnosis(presetTitle: String) {
        _selectedLeafPreset.value = presetTitle
        _selectedLeafBitmap.value = null
        val diag = DiseaseDetectionEngine.getDiagnosisForPreset(presetTitle)
        _diseaseDiagnosis.value = diag
        selectedCropHint.value = diag.crop

        viewModelScope.launch {
            historyDao.insertDiseaseHistory(
                DiseaseHistoryEntity(
                    cropName = diag.crop,
                    diseaseName = diag.diseaseName,
                    pathogen = diag.pathogen,
                    severity = diag.severity,
                    confidence = diag.confidence.toFloat(),
                    remedySummary = diag.organicTreatment.take(120)
                )
            )
        }
    }

    fun runFertilizerCalculation() {
        val acres = fertilizerAcres.value.toFloatOrNull() ?: 1.0f
        val result = FertilizerCalculator.calculate(
            cropName = fertilizerCrop.value,
            soilN = soilN.value,
            soilP = soilP.value,
            soilK = soilK.value,
            acres = acres
        )
        _fertilizerResult.value = result

        viewModelScope.launch {
            historyDao.insertFertilizerHistory(
                FertilizerHistoryEntity(
                    cropName = fertilizerCrop.value,
                    ureaBags = result.ureaBags,
                    dapBags = result.dapBags,
                    mopBags = result.mopBags,
                    organicManureTonnes = result.vermicompostTonnes
                )
            )
        }
    }

    fun performLogin(onSuccess: () -> Unit) {
        if (authPhone.value.length < 10) {
            authError.value = "Please enter a valid 10-digit mobile number"
            return
        }
        if (authPin.value.length < 4) {
            authError.value = "PIN must be at least 4 digits"
            return
        }

        viewModelScope.launch {
            val user = userDao.getUserByPhone(authPhone.value)
            if (user != null && user.pinHash == authPin.value) {
                authError.value = null
                onSuccess()
            } else if (user != null) {
                authError.value = "Incorrect security PIN. Please try again."
            } else {
                authError.value = "Farmer account not found. Please switch to Register."
            }
        }
    }

    fun performRegister(onSuccess: () -> Unit) {
        if (authName.value.isBlank()) {
            authError.value = "Please enter your full name"
            return
        }
        if (authPhone.value.length < 10) {
            authError.value = "Please enter a valid 10-digit mobile number"
            return
        }
        if (authPin.value.length < 4) {
            authError.value = "PIN must be at least 4 digits"
            return
        }

        viewModelScope.launch {
            val acres = authFarmSize.value.toFloatOrNull() ?: 2.0f
            val newUser = UserEntity(
                name = authName.value.trim(),
                phone = authPhone.value.trim(),
                village = authVillage.value.ifBlank { "Smart Krushi Gram" },
                farmSizeAcres = acres,
                soilType = authSoilType.value,
                irrigationType = authIrrigation.value,
                pinHash = authPin.value
            )
            userDao.insertUser(newUser)
            authError.value = null
            onSuccess()
        }
    }

    fun logout() {
        viewModelScope.launch {
            userDao.clearUsers()
            ensureDefaultFarmer()
        }
    }
}
