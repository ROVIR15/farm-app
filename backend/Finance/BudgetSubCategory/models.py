from db_connection import db
from sqlalchemy import Numeric
from datetime import datetime

class BudgetSubCategories(db.Model):
    __tablename__ = "budget_sub_categories"

    id = db.Column(db.Integer(), primary_key=True)
    budget_category_id = db.Column(db.Integer, db.ForeignKey('budget_category.id'), nullable=False)
    sub_category_name = db.Column(db.String(255), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    def __repr__(self):
        return f'<BudgetCategory {self.name}>'
