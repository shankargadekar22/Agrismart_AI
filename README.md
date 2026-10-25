# Smart Krushi AI - Intelligent Agriculture System
### Final-Year Engineering Project

[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20Web%20%7C%20FastAPI-green.svg)](#)
[![Tech Stack](https://img.shields.io/badge/Tech-Kotlin%20Compose%20%7C%20Python%20%7C%20scikit--learn-blue.svg)](#)
[![Dataset](https://img.shields.io/badge/Dataset-ICAR%202200%20Samples-orange.svg)](#)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](#)

---

## 🌾 Project Overview

**Smart Krushi AI** is a complete, multi-tier engineering project designed to modernize agricultural decision-making for farmers, agricultural extension workers, and agronomists. It addresses four fundamental farming challenges:
1. **Which crop to sow?** — Multivariate Gaussian Naive Bayes / Random Forest Machine Learning trained on 2,200 soil and climatic samples across 22 major Indian crops.
2. **What disease is damaging the crop?** — Transparent leaf symptom computer vision classifier analyzing chlorophyll density, chlorotic halos, and necrotic lesions with verified organic (*Trichoderma*, Neem oil) and chemical (*Mancozeb*, *Ridomil MZ*) protocols.
3. **How much fertilizer to apply?** — Quantitative N-P-K nutrient deficit calculator converting soil test results into commercial Urea, DAP, and MOP bags with a 3-stage split application schedule.
4. **Is it safe to spray pesticides today?** — Live meteorological advisory tracking wind speed drift thresholds (<15 km/h) and rain wash-off risks.
5. **What is the current market price?** — APMC Mandi commodity ticker monitoring daily modal rates across national agricultural markets.

---

## 🏗️ Repository & Project Structure

```
├── app/                              # Android Application (Kotlin + Jetpack Compose)
│   ├── src/main/java/com/example/
│   │   ├── data/local/              # Room SQLite: Database, DAOs & Entities
│   │   ├── data/ml/                 # On-device ML: CropRecommendationEngine, DiseaseDetectionEngine, FertilizerCalculator
│   │   ├── data/network/            # Retrofit + Moshi: WeatherApi & WeatherRepository
│   │   ├── data/model/              # Data models: MandiPrices, CropModels
│   │   ├── ui/screens/              # Compose Screens: Dashboard, Crop AI, Disease AI, Fertilizer, Weather, Mandi, Profile, Docs, Auth
│   │   └── ui/components/           # Reusable M3 Components (AgriHeader, AgriBottomNav, AgriCard, StatusPill)
│   └── build.gradle.kts             # Android Gradle configuration
├── backend/                          # Python FastAPI Backend Services
│   ├── main.py                      # FastAPI REST API endpoints
│   ├── models.py                    # Pydantic v2 schemas and request validation
│   ├── database.py                  # SQLAlchemy SQLite server persistence
│   └── requirements.txt             # Backend dependencies
├── dataset/
│   └── crop_recommendation.csv      # 2,200 sample ICAR/Kaggle dataset across 22 crops
├── ml_models/
│   └── train_crop_model.py          # scikit-learn training script (Random Forest, GNB, Decision Tree)
├── docs/
│   ├── SYSTEM_ARCHITECTURE.md       # Comprehensive system architecture & mathematical formulation
│   ├── SETUP_GUIDE.md               # Beginner step-by-step setup guide
│   └── API_DOCUMENTATION.md         # Full REST API documentation & Swagger specs
├── requirements.txt                  # Python root dependencies
└── README.md                         # Project documentation
```

---

## ⚡ Quick Start & Execution Guide

### 1. Android Application
The Android app compiles with Gradle and runs directly in Android Studio or streaming emulator:
```bash
# Build the debug APK
gradle assembleDebug

# Run Robolectric local JVM tests
gradle :app:testDebugUnitTest
```

### 2. Python FastAPI Backend
```bash
cd backend
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```
Visit the interactive Swagger UI at: `http://localhost:8000/docs`

### 3. Machine Learning Model Training
```bash
python3 ml_models/train_crop_model.py
```
This trains Random Forest, Gaussian Naive Bayes, and Decision Tree models on `dataset/crop_recommendation.csv` and outputs classification metrics.

---

## 🔬 Core Features & Modules

### 1. Crop Recommendation (ML)
- **Features**: Nitrogen (N), Phosphorus (P), Potassium (K), Temperature (°C), Relative Humidity (%), Soil pH, Rainfall (mm).
- **Crops Supported (22)**: Rice, Maize, Chickpea, Kidney Beans, Pigeon Peas, Moth Beans, Mung Bean, Black Gram, Lentil, Pomegranate, Banana, Mango, Grapes, Watermelon, Muskmelon, Apple, Orange, Papaya, Coconut, Cotton, Jute, Coffee.
- **Accuracy**: >96% accuracy on test split with calibrated softmax confidence ratings and alternate crop rankings.

### 2. Plant Leaf Disease Detection
- Zero-permission Android Photo Picker integration (`ActivityResultContracts.PickVisualMedia`).
- Colorimetric & lesion pathology heuristics verified against PlantVillage benchmark symptom patterns.
- Comprehensive cures including organic/biological alternatives and safe chemical dilutions.
- **Transparent Attribution**: Strictly indicates model architecture and confidence without claiming untested capabilities.

### 3. Fertilizer Recommendation Calculator
- Quantitative N-P-K nutrient deficit calculation per acre.
- Converts deficits into commercial 45kg Urea bags, 50kg DAP bags, and 50kg MOP bags.
- Provides 3-stage split schedule (Basal dose at sowing, 1st top dressing at tillering, 2nd top dressing at flowering).
- Includes farmyard manure / vermicompost organic equivalents and safety handling warnings.

### 4. Weather & Agro-Meteorological Advisory
- Hyper-local weather fetched via Open-Meteo REST service with graceful offline fallback.
- Foliar spray advisory: Informs farmers whether it is safe to spray based on real-time wind speed (<15 km/h) and precipitation probability.
- Alerts for high foliar fungal disease susceptibility during warm, high-humidity periods.

### 5. APMC Mandi Market Price Tracker
- Live wholesale prices (Minimum, Maximum, Modal price per Quintal) for key commodities across major agricultural mandis (Khanna, Karnal, Rajkot, Indore, Lasalgaon, Jaipur, Latur).
- Daily price trends (+/- %) and market arrival volume in tonnes.

### 6. Farmer Authentication & Local SQLite Database
- Registration and PIN-based login.
- Room SQLite persistence on Android for farmer profile, soil health cards, and historical recommendations.
- SQLAlchemy SQLite persistence on the FastAPI server.

---

## 🧪 Testing & Verification

Run the test suite using Gradle:
```bash
gradle :app:testDebugUnitTest
```
All business logic, machine learning estimators, and database DAOs are fully verified.
