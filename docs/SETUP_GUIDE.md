# Beginner's Setup & Execution Guide
## Smart Krushi AI - Intelligent Agriculture System

This guide provides beginner-friendly instructions to execute, test, and demonstrate every component of the Smart Krushi AI project.

---

## 1. Running the Android Application

### Prerequisites:
- Android Studio Ladybug (or newer) OR Gradle JDK 17+
- Android Device or Streaming Emulator running Android API 24+ (Android 7.0 to Android 15)

### Steps:
1. Open the project root folder in **Android Studio**.
2. Wait for Gradle Sync to complete.
3. Build the project:
   ```bash
   gradle assembleDebug
   ```
4. Run unit and instrumentation tests:
   ```bash
   gradle :app:testDebugUnitTest
   ```
5. Click **Run 'app'** or install via ADB:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 2. Running the Python FastAPI Backend

### Prerequisites:
- Python 3.9, 3.10, 3.11, or 3.12
- `pip` package manager

### Steps:
1. Navigate to the `backend/` directory:
   ```bash
   cd backend
   ```
2. Create and activate a virtual environment:
   ```bash
   python3 -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```
3. Install required dependencies:
   ```bash
   pip install -r requirements.txt
   ```
4. Start the FastAPI development server:
   ```bash
   uvicorn main:app --reload --host 0.0.0.0 --port 8000
   ```
5. Open interactive Swagger API documentation in your browser:
   ```
   http://localhost:8000/docs
   ```

---

## 3. Training & Evaluating the Machine Learning Model

### Steps:
1. Ensure your Python virtual environment has dependencies installed:
   ```bash
   pip install scikit-learn numpy pandas joblib
   ```
2. Run the training script:
   ```bash
   python3 ml_models/train_crop_model.py
   ```
3. The script will:
   - Verify/generate the 2,200 sample `dataset/crop_recommendation.csv` file.
   - Train Random Forest, Gaussian Naive Bayes, and Decision Tree classifiers.
   - Output test set accuracy scores and detailed classification metrics.
   - Export the serialized model to `ml_models/crop_model.joblib`.

---

## 4. Verification and Viva Defense Checklist

| Feature | Verification Method in App | Expected Result |
| :--- | :--- | :--- |
| **Crop Recommendation** | Navigate to 'Crop AI' -> Tap 'Alluvial Loam' preset -> Tap 'Recommend Best Crop' | Rice or Maize predicted with >90% match, duration, yield, and soil factors |
| **Disease Detection** | Navigate to 'Disease AI' -> Tap 'Tomato Early Blight' card | Immediate pathology report, pathogen *Alternaria solani*, biological & chemical treatments |
| **Fertilizer Calculator** | Navigate to 'Fertilizer' -> Set 2.0 Acres, select 'Rice' -> Calculate | Bag counts for Urea, DAP, MOP, plus 3-stage split schedule |
| **Agro Weather** | Navigate to 'Weather' -> Select 'Pune' | Temperature, humidity, 5-day forecast, and spray-safe advisory |
| **Mandi Market Rates** | Navigate to 'Dashboard' or 'Weather' -> 'Check Mandi Prices' | Daily APMC modal rates for Wheat, Cotton, Soybean with price trends |
| **Farmer Profile & Auth** | Tap Profile icon -> Enter test credentials (Phone: 9876543210, PIN: 1234) | Farmer details loaded and recommendations saved to Room SQLite |
