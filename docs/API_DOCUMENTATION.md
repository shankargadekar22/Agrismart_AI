# REST API Documentation
## Smart Krushi AI - FastAPI Backend Services

The FastAPI backend exposes endpoints for crop recommendation, disease classification, fertilizer calculation, and farmer authentication.

Base URL: `http://localhost:8000` (Swagger UI: `http://localhost:8000/docs`)

---

### 1. Crop Recommendation Endpoint
- **URL**: `/predict-crop`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Request Body**:
  ```json
  {
    "nitrogen": 85.0,
    "phosphorus": 48.0,
    "potassium": 40.0,
    "temperature": 24.5,
    "humidity": 78.0,
    "ph": 6.5,
    "rainfall": 180.0
  }
  ```
- **Response**: `200 OK`
  ```json
  {
    "status": "success",
    "recommended_crop": "Rice (Paddy)",
    "confidence_percent": 96,
    "alternate_crops": [
      {"crop": "Jute", "confidence": 18},
      {"crop": "Maize (Corn)", "confidence": 12}
    ],
    "season": "Kharif",
    "expected_yield": "3.5 - 5.0 tonnes/ha",
    "agronomic_advisory": "Ideal soil profile match for Rice (Paddy)..."
  }
  ```

---

### 2. Fertilizer Calculation Endpoint
- **URL**: `/fertilizer-calc`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Request Body**:
  ```json
  {
    "crop_name": "Rice (Paddy)",
    "soil_n": 40.0,
    "soil_p": 20.0,
    "soil_k": 22.0,
    "acres": 2.0
  }
  ```
- **Response**: `200 OK`
  ```json
  {
    "crop_name": "Rice (Paddy)",
    "farm_area_acres": 2.0,
    "urea_45kg_bags": 3.0,
    "dap_50kg_bags": 2.0,
    "mop_50kg_bags": 1.0,
    "vermicompost_tonnes": 3.0,
    "split_schedule": [
      {
        "stage_name": "Basal Application",
        "timing": "At Sowing / Final Land Prep",
        "urea_bags": "1 bags",
        "dap_bags": "2 bags (100%)",
        "mop_bags": "1 bags",
        "advisory": "Incorporate 5-8 cm deep into soil before seed drill."
      }
    ],
    "safety_guidelines": [
      "Never mix DAP with Zinc Sulphate directly in spray tank."
    ]
  }
  ```

---

### 3. Plant Leaf Disease Detection Endpoint
- **URL**: `/predict-disease`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **Form Data**:
  - `file`: (Binary image file)
  - `crop_hint`: "Tomato"
- **Response**: `200 OK`
  ```json
  {
    "crop": "Tomato",
    "disease_name": "Tomato Early Blight",
    "pathogen": "Alternaria solani (Fungal)",
    "severity": "Moderate to High",
    "confidence_percent": 92,
    "symptoms": "Concentric dark brown rings with chlorotic yellow halo...",
    "organic_remedy": "Spray Trichoderma viride (10g/L) or cold-pressed Neem oil...",
    "chemical_remedy": "Apply Mancozeb 75% WP @ 2.5 g/L...",
    "safety_precautions": "Wear respirator and gloves. Observe 7-day PHI.",
    "model_architecture": "Colorimetric Pathology Heuristic Model (Verified on PlantVillage benchmarks)"
  }
  ```

---

### 4. Farmer Authentication Endpoints
- **Register**: `POST /auth/register`
- **Login**: `POST /auth/login`
