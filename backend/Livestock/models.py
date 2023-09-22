from sqlalchemy import func
from datetime import datetime
from db_connection import db


class Livestock (db.Model):
    __tablename__ = "livestock"

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(100), nullable=False)
    gender = db.Column(db.Integer, nullable=False)
    bangsa = db.Column(db.String(50), nullable=False)
    description = db.Column(db.Text, nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
    # info = db.relationship('BlockAreaSledLivestock', backref='livestock', lazy=True)

    weight_records = db.relationship('WeightRecord', back_populates='livestock', lazy=True)
    bcs_records = db.relationship('BCSRecord', back_populates='livestock', lazy=True)
    health_records = db.relationship('HealthRecord', back_populates='livestock', lazy=True)

def __repr__(self):
    return f'<Livestock {self.name}>'
