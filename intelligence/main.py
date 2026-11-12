"""Service d'intelligence SHS : routes SIMULÉES (semaine 1). Les vraies règles arrivent en semaines 2 à 4."""
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(title="SHS intelligence (simulé)")


class RankIn(BaseModel):
    candidates: list[dict] = []
    importances: dict = {}


class TrustIn(BaseModel):
    listing_id: str


class ProofIn(BaseModel):
    document_type: str
    file_hash: str


class CopilotIn(BaseModel):
    user_id: str
    task: str
    payload: dict = {}


@app.get("/health")
def health():
    return {"status": "ok", "simulated": True}


@app.post("/internal/rank")
def rank(body: RankIn):
    items = [
        {"listingId": c.get("id"), "compatibilityPercent": 80, "strengths": [], "cautions": []}
        for c in body.candidates
    ]
    return {"items": items}


@app.post("/internal/trust")
def trust(body: TrustIn):
    return {"riskScore": 20, "level": "LOW", "reasons": [], "simulated": True}


@app.post("/internal/proof/verify")
def proof_verify(body: ProofIn):
    return {"status": "VERIFIED", "extractedName": None, "extractedAddress": None, "consistencyScore": 1.0}


@app.post("/internal/copilot/run")
def copilot_run(body: CopilotIn):
    return {
        "text": "Réponse simulée. Copilot peut se tromper. Vérifiez les informations avant de vous engager.",
        "certainty": "LOW",
        "listings": [],
    }
