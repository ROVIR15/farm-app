from db_connection import db
from sqlalchemy import func
from datetime import datetime


class WeightRecord(db.Model):
    __tablename__ = "weight_record"

    id = db.Column(db.Integer(), primary_key=True)
    livestock_id = db.Column(db.Integer(), db.ForeignKey('livestock.id'), nullable=False)
    date = db.Column(db.Date(), nullable=False)
    score = db.Column(db.Float(), nullable=False)
    remarks = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    livestock = db.relationship('Livestock', back_populates='weight_records', lazy=True)

    def __repr__(self):
        return f'<WeightRecord {self.name}>'
