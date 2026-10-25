# System Architecture: Smart Krushi AI - Intelligent Agriculture System

## 1. Executive Summary
Smart Krushi AI is a multi-tier engineering project designed to modernize Indian agricultural decision-making. By integrating statistical machine learning, computer vision heuristics, meteorological APIs, and local ACID persistence, it empowers smallholder and commercial farmers to optimize resource allocation, prevent disease outbreaks, and maximize economic returns.

---

## 2. Multi-Tier Architecture Diagram

```
+------------------------------------------------------------------------+
|                          CLIENT APPLICATION                            |
|  - Native Android App: Kotlin + Jetpack Compose (Material Design 3)   |
|  - Edge-to-Edge Navigation, Zero-Permission Photo Picker (Play Policy)  |
|  - On-Device Machine Learning: Multivariate Gaussian Classifier        |
|  - Local Persistence: Android Room SQLite (ACID Compliant)             |
+-----------------------------------+------------------------------------+
                                    | REST (JSON / Multipart)
                                    v
+------------------------------------------------------------------------+
|                       BACKEND MICROSERVICE (FastAPI)                   |
|  - Python 3.10+ ASGI Engine (Uvicorn)                                  |
|  - Pydantic v2 Input Validation & Serialization                        |
|  - CORS Security Middleware & Router Modularization                    |
|  - Endpoints: /predict-crop, /predict-disease, /fertilizer-calc,       |
|               /weather, /auth/register, /auth/login                    |
+-------------------+--------------------------------+-------------------+
                    |                                |
                    v                                v
+-----------------------------------+  +---------------------------------+
|      MACHINE LEARNING PIPELINE     |  |       DATABASE PERSISTENCE      |
| - scikit-learn Random Forest      |  | - SQLite Server Database        |
| - Gaussian Naive Bayes Model      |  | - SQLAlchemy 2.0 ORM            |
| - 22-Crop ICAR Benchmark Dataset  |  | - Farmers, Crop History &       |
| - Computer Vision Symptom Engine  |  |   Fertilizer Dose Records       |
+-----------------------------------+  +---------------------------------+
```

---

## 3. Core Component Specifications

### 3.1 Crop Recommendation Engine
- **Input Features (7 continuous variables)**:
  - Soil Nitrogen ($N$, $0 - 140$ kg/ha)
  - Soil Phosphorus ($P$, $5 - 145$ kg/ha)
  - Soil Potassium ($K$, $5 - 205$ kg/ha)
  - Ambient Temperature ($8.8^\circ\text{C} - 43.7^\circ\text{C}$)
  - Relative Humidity ($14.2\% - 99.9\%$)
  - Soil pH ($3.5 - 9.5$)
  - Rainfall ($20 - 300$ mm)
- **Mathematical Formulation**:
  Class conditional probability using Gaussian distributions:
  $$P(x_i \mid C_k) = \frac{1}{\sqrt{2\pi\sigma_{k,i}^2}} \exp\left( -\frac{(x_i - \mu_{k,i})^2}{2\sigma_{k,i}^2} \right)$$
  Log-likelihood accumulation:
  $$\ln P(C_k \mid X) \propto \sum_{i=1}^{7} \left( -\frac{(x_i - \mu_{k,i})^2}{2\sigma_{k,i}^2} - \ln \sigma_{k,i} \right)$$
  Softmax normalization across top-ranked candidates provides calibrated percentage confidence.

### 3.2 Crop Leaf Disease Detection
- Zero-permission Android Photo Picker ingestion (`ActivityResultContracts.PickVisualMedia`).
- Colorimetric & pathological symptom analysis:
  - Green chlorophyll density index ($G > 1.15 \times R \text{ and } G > 1.15 \times B$).
  - Chlorotic yellow halo extraction ($R > 130, G > 120, B < 100$).
  - Necrotic lesion boundary detection ($R \in [60, 150], G \in [40, 110], B < 80$).
- Curative protocols:
  - **Biological/Organic**: *Trichoderma viride*, *Pseudomonas fluorescens*, cold-pressed Neem Oil (3000 ppm).
  - **Chemical**: Mancozeb 75% WP, Metalaxyl 8% + Mancozeb 64% WP (Ridomil MZ), Propiconazole 25% EC with strict Pre-Harvest Interval (PHI) compliance.

### 3.3 Fertilizer Recommendation Calculator
- Deficit algorithm based on ICAR nutrient removal standards per acre:
  $$\text{Deficit}_N = \max(0, \text{Target}_N - \text{Soil}_N \times 0.40)$$
  $$\text{Deficit}_P = \max(0, \text{Target}_P - \text{Soil}_P \times 0.35)$$
  $$\text{Deficit}_K = \max(0, \text{Target}_K - \text{Soil}_K \times 0.45)$$
- Translates deficits into 50kg DAP, 45kg Urea, and 50kg MOP bags with a 3-stage split application schedule (Basal, 1st Tillering Top Dressing, 2nd Flowering Top Dressing).

### 3.4 Agro-Meteorological Advisory
- Open-Meteo REST API integration for real-time 5-day hyper-local agricultural forecasts.
- Evaluates foliar spray safety window:
  - Wind speed $< 15$ km/h (drift threshold)
  - Precipitation probability $< 50\%$ (wash-off threshold)
- Fungal outbreak alert triggered when humidity $> 75\%$ and temperature $\in [20, 30]^\circ\text{C}$.

---

## 4. Engineering Standards & Compliance
- Jetpack Compose Material Design 3 guidelines.
- Clean Architecture (MVVM with StateFlow & Flow).
- Zero broad storage permissions (`READ_EXTERNAL_STORAGE` strictly avoided).
- Local fallback resilience (full functionality maintained even during internet connectivity dropouts).
