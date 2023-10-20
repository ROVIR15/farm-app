from db_connection import db
from datetime import datetime


class HasExpenditure (db.Model):
    __tablename__ = 'farm_profile_has_expenditure'

    id = db.Column(db.Integer(), primary_key=True)
    farm_profile_id = db.Column(
        db.Integer(), db.ForeignKey('farm_profile.id'), nullable=False)
    expenditure_id = db.Column(
        db.Integer(), db.ForeignKey('expenditure.id'), primary_key=True) #db.ForeignKey('breeding.id')
