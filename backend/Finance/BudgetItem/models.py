from db import db
from utils.index import CustomDateTimeField

class BudgetItem (db.Model):
    __tablename__ = "budget_item"

    id = db.Column(db.Integer(), primary_key=True)
    budget_id = db.Column(db.Integer(), db.ForeignKey('budget.id'),nullable=False)
    budget_category_id = db.Column(db.Integer(), db.ForeignKey('budget_category.id'),nullable=False)
    amount = db.Column(db.Float(), nullable=False)
    created_at = CustomDateTimeField()
