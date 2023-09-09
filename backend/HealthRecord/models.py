from db import db
from sqlalchemy import Numeric
from datetime import datetime

class HealthRecord (db.Model):
    id = db.Column(db.Integer, primary_key=True)
    livestock_id = db.Column(db.Integer, nullable=False)
    disease_type = db.Column(db.Integer, nullable=False)
    remarks = db.Column(db.Text, nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

def __repr__(self):
    return f'<HealthRecord {self.id}>'
