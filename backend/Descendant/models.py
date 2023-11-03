from db_connection import db
from sqlalchemy import func
from datetime import datetime

class Descendant (db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    livestock_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'), nullable=False)
    livestock_male_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'), nullable=False)
    livestock_female_id = db.Column(
        db.Integer(), db.ForeignKey('livestock.id'), nullable=False)

    livestock = db.relationship('Livestock', foreign_keys=[livestock_id])
    parent_male = db.relationship('Livestock', foreign_keys=[livestock_male_id])
    parent_female = db.relationship('Livestock', foreign_keys=[livestock_female_id])
