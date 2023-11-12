from db_connection import db
from datetime import datetime


class BreedingHistory (db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    breeding_id = db.Column(
        db.Integer(), db.ForeignKey('breeding.id'), nullable=False)
    remarks = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
    date = db.Column(db.Date(), nullable=False)

    breeding = db.relationship('Breeding', back_populates='breeding_history', lazy=True)