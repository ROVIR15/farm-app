from db_connection import db
from datetime import datetime


class HasIncome (db.Model):
    __tablename__ = 'farm_profile_has_income'


    farm_profile_id = db.Column(
        db.Integer(), db.ForeignKey('farm_profile.id'), nullable=False)
    income_id = db.Column(
        db.Integer(), db.ForeignKey('income.id'), primary_key=True) #db.ForeignKey('breeding.id')

    income = db.relationship('Income', foreign_keys=[income_id])