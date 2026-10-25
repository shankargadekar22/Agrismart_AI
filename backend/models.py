from pydantic import BaseModel, Field, field_validator
from typing import List, Optional, Dict, Any

class CropPredictionRequest(BaseModel):
    nitrogen: float = Field(..., ge=0, le=200, description="Nitrogen ratio in soil (kg/ha)")
    phosphorus: float = Field(..., ge=0, le=200, description="Phosphorus ratio in soil (kg/ha)")
    potassium: float = Field(..., ge=0, le=250, description="Potassium ratio in soil (kg/ha)")
    temperature: float = Field(..., ge=-10, le=60, description="Temperature in Celsius")
    humidity: float = Field(..., ge=0, le=100, description="Relative humidity percentage")
    ph: float = Field(..., ge=2.0, le=12.0, description="Soil pH level")
    rainfall: float = Field(..., ge=0, le=500, description="Rainfall in mm")

class AlternateCropItem(BaseModel):
    crop: str
    confidence: int

class CropPredictionResponse(BaseModel):
    status: str
    recommended_crop: str
    confidence_percent: int
    alternate_crops: List[AlternateCropItem]
    season: str
    expected_yield: str
    agronomic_advisory: str

class FertilizerRequest(BaseModel):
    crop_name: str
    soil_n: float = Field(..., ge=0)
    soil_p: float = Field(..., ge=0)
    soil_k: float = Field(..., ge=0)
    acres: float = Field(default=1.0, gt=0)

class SplitStageItem(BaseModel):
    stage_name: str
    timing: str
    urea_bags: str
    dap_bags: str
    mop_bags: str
    advisory: str

class FertilizerResponse(BaseModel):
    crop_name: str
    farm_area_acres: float
    urea_45kg_bags: float
    dap_50kg_bags: float
    mop_50kg_bags: float
    vermicompost_tonnes: float
    split_schedule: List[SplitStageItem]
    safety_guidelines: List[str]

class DiseasePredictionResponse(BaseModel):
    crop: str
    disease_name: str
    pathogen: str
    severity: str
    confidence_percent: int
    symptoms: str
    organic_remedy: str
    chemical_remedy: str
    safety_precautions: str
    model_architecture: str

class UserRegister(BaseModel):
    name: str
    phone: str = Field(..., min_length=10, max_length=10)
    village: str
    farm_size_acres: float
    soil_type: str
    irrigation_type: str
    pin: str = Field(..., min_length=4)

class UserLogin(BaseModel):
    phone: str = Field(..., min_length=10, max_length=10)
    pin: str = Field(..., min_length=4)
