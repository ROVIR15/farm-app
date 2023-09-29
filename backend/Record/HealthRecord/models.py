from db_connection import db
from sqlalchemy import func
from datetime import datetime


class HealthRecord(db.Model):
    __tablename__ = "health_record"

    id = db.Column(db.Integer(), primary_key=True)
    livestock_id = db.Column(db.Integer(), db.ForeignKey(
        'livestock.id'), nullable=False)
    date = db.Column(db.Date(), nullable=False)
    disease_type = db.Column(db.Integer(), nullable=False)
    treatment_methods = db.Column(db.Text(), nullable=False)
    remarks = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    livestock = db.relationship('Livestock', foreign_keys=[livestock_id], lazy=True)

    def __repr__(self):
        return f'<HealthRecord {self.name}>'
