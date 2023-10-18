from db_connection import db
from datetime import datetime


class Pregnancy (db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    breeding_id = db.Column(
        db.Integer(), db.ForeignKey('breeding.id'), nullable=False)
    is_active = db.Column(
        db.Boolean(), nullable=False) #db.ForeignKey('breeding.id')
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    breeding = db.relationship('Breeding', foreign_keys=[breeding_id], back_populates='pregnancy', lazy=True)