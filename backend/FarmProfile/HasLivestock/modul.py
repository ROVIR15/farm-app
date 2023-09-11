from db import db
from datetime import datetime


class HasLivestock (db.Model):
    __tablename__ = 'farm_profile_has_livestock'

    id = db.Column(db.Integer(), primary_key=True)
    farm_profile_id = db.Column(
        db.Integer(), db.ForeignKey('farm_profile.id'), nullable=False)
    livestock_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'), db.ForeignKey('livestock.id'), nullable=False)  # db.ForeignKey('breeding.id')
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
