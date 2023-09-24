from db_connection import db
from datetime import datetime


class HasBreeding (db.Model):
    __tablename__ = 'farm_profile_has_breeding'

    id = db.Column(db.Integer(), primary_key=True)
    farm_profile_id = db.Column(
        db.Integer(), db.ForeignKey('farm_profile.id'), nullable=False)
    breeding_id = db.Column(
        db.Integer(), db.ForeignKey('breeding.id'), db.ForeignKey('breeding.id'), nullable=False)  # db.ForeignKey('breeding.id')
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
