from db_connection import db
from datetime import datetime


class HasBudgetItem (db.Model):
    __tablename__ = 'farm_profile_has_budget_item'

    farm_profile_id = db.Column(
        db.Integer(), db.ForeignKey('farm_profile.id'), nullable=False)
    budget_item_id = db.Column(
        db.Integer(), db.ForeignKey('budget_item.id'), primary_key=True, nullable=False)  # db.ForeignKey('breeding.id')

    budget_item = db.relationship(
        'BudgetItem', foreign_keys=[budget_item_id])
