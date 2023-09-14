from db_connection import db
from datetime import datetime


class BreedingStatus (db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    breeding_id = db.Column(
        db.Integer(), db.ForeignKey('breeding.id'), nullable=False)
    breeding_status_name_id = db.Column(
        db.Integer(), nullable=False) #db.ForeignKey('breeding.id')
    remarks = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
