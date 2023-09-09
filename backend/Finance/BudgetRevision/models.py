from db import db
from utils.index import CustomDateTimeField

class BudgetRevision (db.Model):
    __tablename__ = "budget_revision"

    id = db.Column(db.Integer(), primary_key=True)
    budget_id = db.Column(db.Integer(), db.ForeignKey('budget.id'),nullable=False)
    budget_category_id = db.Column(db.Integer(), db.ForeignKey('budget_category.id'),nullable=False)
    from_amount = db.Column(db.Float(), nullable=False)
    to_amount = db.Column(db.Float(), nullable=False)
    notes = db.Column(db.Text(), nullable=False)
    created_at = CustomDateTimeField()
