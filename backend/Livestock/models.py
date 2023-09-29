from sqlalchemy import func, desc
from datetime import datetime
from db_connection import db

from Record.WeightRecord.models import WeightRecord
from Record.BCSRecord.models import BCSRecord
from Record.HealthRecord.models import HealthRecord

class Livestock (db.Model):
    __tablename__ = "livestock"

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(100), nullable=False)
    gender = db.Column(db.Integer, nullable=False)
    bangsa = db.Column(db.String(50), nullable=False)
    description = db.Column(db.Text, nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
    # info = db.relationship('BlockAreaSledLivestock', backref='livestock', lazy=True)

    weight_records = db.relationship('WeightRecord', back_populates='livestock', order_by=desc(WeightRecord.created_at), lazy=True)
    bcs_records = db.relationship('BCSRecord', back_populates='livestock', order_by=desc(BCSRecord.created_at), lazy=True)
    health_records = db.relationship('HealthRecord', back_populates='livestock', order_by=desc(HealthRecord.created_at), lazy=True)

def __repr__(self):
    return f'<Livestock {self.name}>'
