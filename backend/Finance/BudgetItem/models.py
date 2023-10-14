from db_connection import db
from sqlalchemy import Numeric
from datetime import datetime
from Finance.BudgetCategory.models import BudgetCategory

class BudgetItem (db.Model):
    __tablename__ = "budget_item"

    id = db.Column(db.Integer, primary_key=True)
    month_year = db.Column(db.Date, nullable=False)
    budget_category_id = db.Column(db.Integer, db.ForeignKey('budget_category.id'), nullable=False)
    amount = db.Column(Numeric(precision=10, scale=4), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    # Define the `block_area` relationship
    budget_category = db.relationship(BudgetCategory, foreign_keys=[budget_category_id], lazy=True)

    def __repr__(self):
        return f'<BudgetItem {self.name}>'