from db_connection import db
from datetime import datetime


class Lambing(db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    breeding_id = db.Column(db.Integer(), db.ForeignKey('breeding.id'), nullable=False)
    livestock_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
    
    livestock = db.relationship('Livestock', foreign_keys=[livestock_id])
    breeding = db.relationship('Breeding', foreign_keys=[breeding_id], back_populates='lambing', lazy=True)
