from db_connection import db
from datetime import datetime


class Descendants (db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    breeding_id = db.Column(
        db.Integer(), db.ForeignKey('breeding.id'), nullable=False)
    livestock_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'), nullable=False) #db.ForeignKey('breeding.id')
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
