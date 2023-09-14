from db import db
from datetime import datetime


class HasSled (db.Model):
    __tablename__ = 'farm_profile_has_sled'

    id = db.Column(db.Integer(), primary_key=True)
    farm_profile_id = db.Column(
        db.Integer(), db.ForeignKey('farm_profile.id'), nullable=False)
    sled_id = db.Column(
        db.Integer(), db.ForeignKey('sled.id'), db.ForeignKey('sled.id'), nullable=False) #db.ForeignKey('breeding.id')
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
