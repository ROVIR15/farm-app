from db_connection import db
from datetime import datetime


class Breeding (db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    livestock_male_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'), nullable=False)
    livestock_female_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'), nullable=False)
    sled_id = db.Column(db.Integer(), db.ForeignKey('sled.id'), nullable=False)
    is_active = db.Column(db.Boolean(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)