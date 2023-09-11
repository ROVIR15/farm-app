from db import db
from datetime import datetime


class BreedingHistory (db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    breeding_id = db.Column(
        db.Integer(), db.ForeignKey('breeding.id'), nullable=False)
    remarks = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
