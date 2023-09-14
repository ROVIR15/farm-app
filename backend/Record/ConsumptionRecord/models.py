from db import db
from sqlalchemy import Numeric
from datetime import datetime

class ConsumptionRecord (db.Model):
    id = db.Column(db.Integer, primary_key=True)
    block_area_d = db.Column(db.Integer, nullable=False)
    sku_id = db.Column(db.Integer, db.ForeignKey('sku.id'), nullable=False)
    score = db.Column(db.Numeric(precision=4, scale=2), nullable=False)
    remarks = db.Column(db.Text, nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

def __repr__(self):
    return f'<ConsumptionRecord {self.id}>'
