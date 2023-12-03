from sqlalchemy import desc
from db_connection import db
from datetime import datetime
from Breeds.Pregnancy.models import Pregnancy

from Breeds.BreedingHistory.models import BreedingHistory
from Breeds.BreedingStatus.models import BreedingStatus

class Breeding (db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    date = db.Column(db.Date(), nullable=False)
    livestock_male_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'))
    livestock_female_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'), nullable=False)
    sled_id = db.Column(db.Integer(), db.ForeignKey('sled.id'), nullable=False)
    is_active = db.Column(db.Boolean(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    livestock_male = db.relationship('Livestock', foreign_keys=[livestock_male_id])
    livestock_female = db.relationship('Livestock', foreign_keys=[livestock_female_id])
    pregnancy = db.relationship('Pregnancy', back_populates='breeding', order_by=desc(Pregnancy.id), cascade="all, delete")
    breeding_status = db.relationship('BreedingStatus', back_populates='breeding', order_by=desc(BreedingStatus.id), cascade="all, delete")
    breeding_history = db.relationship('BreedingHistory', back_populates='breeding', order_by=desc(BreedingHistory.id), cascade="all, delete")
    lambing = db.relationship('Lambing', back_populates='breeding')
    sled = db.relationship('Sled', foreign_keys=[sled_id])