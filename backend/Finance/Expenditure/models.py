from db import db
from utils.index import CustomDateTimeField

class BudgetItem (db.Model):
    __tablename__ = "Expenditure"

    id = db.Column(db.Integer(), primary_key=True)
    budget_id = db.Column(db.Integer(), db.ForeignKey('budget.id'),nullable=False)
    budget_item_id = db.Column(db.Integer(), db.ForeignKey('budget_item.id'),nullable=False)
    sku_id = db.Column(db.Integer(), db.ForeignKey('sku.id'),nullable=False)
    amount = db.Column(db.Float(), nullable=False)
    description = db.Column(db.Text(), nullable=False)
    created_at = CustomDateTimeField()
